package com.romeo.eatmeapp.adminpanel

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.romeo.eatmeapp.MainActivity
import com.romeo.eatmeapp.databinding.ActivityAdminBinding
import com.romeo.eatmeapp.services.MusicService
import androidx.core.content.edit

class AdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminBinding
    private var musicBound = false
    private lateinit var musicService: MusicService

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

            (applicationContext as? MainActivity)?.isTestMode = isChecked

            sharedPref.edit() { putBoolean("is_test_mode", isChecked) }
            Toast.makeText(this,
                "Репозиторий: ${if (isChecked) "Тестовый" else "Боевой"}",
                Toast.LENGTH_SHORT).show()
        }

        binding.btnBack.setOnClickListener {
            finish()
            Intent(this, MainActivity::class.java).also {
                startActivity(it)
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
}