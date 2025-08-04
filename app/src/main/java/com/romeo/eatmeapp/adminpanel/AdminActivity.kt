package com.romeo.eatmeapp.adminpanel

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.romeo.eatmeapp.AppApplication
import com.romeo.eatmeapp.MainActivity
import com.romeo.eatmeapp.RestaurantDataObject
import com.romeo.eatmeapp.databinding.ActivityAdminBinding
import com.romeo.eatmeapp.services.MusicService
import com.romeo.eatmeapp.ui.menu.MenuViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.getValue


class AdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminBinding
    private var musicBound = false
    private lateinit var musicService: MusicService


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: AdminActivityViewModel by viewModels { viewModelFactory }


    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MusicService.LocalBinder
            musicService = binder.getService()
            musicBound = true
            updateSwitchState()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            musicBound = false
        }
    }




    @SuppressLint("ImplicitSamInstance")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as AppApplication)
            .appComponent
            .inject(this)


        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val sharedPref = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val isMusicEnabled = sharedPref.getBoolean("music_enabled", true)
        val isTestMode = sharedPref.getBoolean("is_test_mode", false)

        binding.switchMusic.isChecked = isMusicEnabled
        binding.switchRepository.isChecked = isTestMode

        Intent(this, MusicService::class.java).also { intent ->
            bindService(intent, serviceConnection, BIND_AUTO_CREATE)
        }

        binding.switchMusic.setOnCheckedChangeListener { _, isChecked ->
            (applicationContext as? MainActivity)?.musicEnabled = isChecked
            sharedPref.edit() { putBoolean("music_enabled", isChecked) }

            if (isChecked) {
                if (!MusicService.isRunning)
                    startService(Intent(this, MusicService::class.java))
                musicService.resumeMusic()
            } else {
                musicService.pauseMusic()
                if (!musicService.isMusicPlaying())
                    stopService(Intent(this, MusicService::class.java))
            }
        }


        binding.switchRepository.setOnCheckedChangeListener { _, isChecked ->
            val app = applicationContext as AppApplication

            app.isTestMode = isChecked

            sharedPref.edit() { putBoolean("is_test_mode", isChecked) }

            Toast.makeText(this,
                "Репозиторий: ${if (isChecked) "Тестовый" else "Боевой"}",
                Toast.LENGTH_SHORT).show()

            app.recreateAppComponent()
            lifecycleScope.launch {
                try {
                    val newRepository = if (isChecked) {
                        app.appComponent.provideFakeRepository()
                    } else {
                        app.appComponent.provideRealRepository()
                    }
                    RestaurantDataObject.forceReload(newRepository)
                } catch (e: Exception) {
                    Log.e("AdminActivity", "Failed to reload restaurant data. Error: $e")
                }
            }

        }

        binding.btnBack.setOnClickListener {
            finish()
            Intent(this, MainActivity::class.java).also {
                startActivity(it)
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.restaurant.collect { restaurant ->
                    restaurant?.let {
                        binding.locationId.text = "ID локации: ${it.locationId}"
                        binding.locationName.text = "Название локоации: ${it.locationName}"
                        binding.locationAddress.text = "Адрес локации: ${it.address}"
                        binding.hasGameZone.text = if (it.hasGameZone) "Есть игровая зона" else "Нет игровой зоны"
                    }
                }
            }
        }



    }

    private fun updateSwitchState() {
        if (musicBound) {
            binding.switchMusic.isChecked = musicService.isMusicPlaying()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(serviceConnection)
        musicBound = false
    }

    companion object {
        const val EXTRA_IS_TEST_MODE = "is_test_mode"
    }


}