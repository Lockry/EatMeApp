package com.romeo.eatmeapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.romeo.eatmeapp.databinding.ActivityMainBinding
import com.romeo.eatmeapp.services.MusicService
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.romeo.eatmeapp.ui.dialogs.InfoDialog
import com.romeo.eatmeapp.ui.nointernet.NetworkStatus

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel

    private val prefs by lazy { getSharedPreferences("app_prefs", MODE_PRIVATE) }

    private var inactivityJob: Job? = null
    private val timerSplashScreen = 10_000L // 10 сек

    var musicEnabled = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setOnApplyWindow()
        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]

        loadPrefs()

        setUpMusicService()

        resetInactivityTimer()
    }

    override fun onResume() {
        super.onResume()
        loadPrefs()
    }

    override fun onStart() {
        super.onStart()
        observeNetworkChanges()
        resetInactivityTimer()
        if (musicEnabled) {
            startService(Intent(this, MusicService::class.java))
        }
    }

    @SuppressLint("ImplicitSamInstance")
    override fun onStop() {
        inactivityJob?.cancel()
        super.onStop()
        if (musicEnabled && MusicService.isRunning) {
            stopService(Intent(this, MusicService::class.java))
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        resetInactivityTimer()
        return super.dispatchTouchEvent(ev)
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        resetInactivityTimer()
    }

    private fun observeNetworkChanges() {
        val navController = findNavController(R.id.nav_host_fragment)

        lifecycleScope.launch {
            viewModel.networkStatus.collect { status ->
                val currentDestination = navController.currentDestination?.id

                when (status) {
                    NetworkStatus.Available -> {
                        if (currentDestination == R.id.noInternetFragment) {
                            navController.popBackStack()
                        }
                    }

                    NetworkStatus.Lost -> {
                        if (currentDestination != R.id.noInternetFragment) {
                            navController.navigate(R.id.noInternetFragment)
                        }
                    }
                }
            }
        }
    }

    private fun resetInactivityTimer() {
        inactivityJob?.cancel()
        inactivityJob = lifecycleScope.launch {
            delay(timerSplashScreen)
            if (shouldNavigateToSplash()) {
                if (InfoDialog.isShowing()) {
                    InfoDialog.dismiss()
                }
                findNavController(R.id.nav_host_fragment)
                    .navigate(R.id.action_go_toSplashScreen)
            }
        }
    }

    private fun shouldNavigateToSplash(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        val currentDestination = navController.currentDestination?.id
        return currentDestination != R.id.splashFragment &&
                currentDestination != R.id.noInternetFragment
    }


    private fun setUpMusicService() {
        val intent = Intent(this, MusicService::class.java)
        if (musicEnabled) {
            startService(intent)
        } else {
            stopService(intent)
        }
    }


    private fun loadPrefs() {
        musicEnabled = prefs.getBoolean("music_enabled", true)
    }

    private fun setOnApplyWindow() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.nav_host_fragment)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
