package com.romeo.eatmeapp

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
import com.romeo.eatmeapp.ui.nointernet.NetworkStatus
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    var isTestMode = true
    var musicEnabled = false

    private val timerSpashScreen = 10000L // 5 сек
    private var inactivityJob: Job? = null

    private val prefs by lazy { getSharedPreferences("app_prefs", MODE_PRIVATE) }

    private lateinit var viewModel: MainActivityViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.nav_host_fragment)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        resetInactivityTimer()
        observeNetworkChanges()

        isTestMode = prefs.getBoolean("is_test_mode", true)
        musicEnabled = prefs.getBoolean("music_enabled", true)


        if (musicEnabled) {
            Intent(this, MusicService::class.java).also {
                startService(it)
            }
        } else  {
            Intent(this, MusicService::class.java).also {
                stopService(it)
            }
        }
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
        inactivityJob?.cancel() // Отменяем предыдущую задачу

        inactivityJob = lifecycleScope.launch {
            delay(timerSpashScreen)

            val navController = findNavController(R.id.nav_host_fragment)
            val currentDestination = navController.currentDestination?.id

            if (currentDestination != R.id.noInternetFragment) {
                if (currentDestination != R.id.splashFragment) {
                    navController.navigate(R.id.action_go_toSplashScreen)
                }
            }
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

    override fun onStop() {
        inactivityJob?.cancel()
        super.onStop()
    }

    override fun onStart() {
        super.onStart()
        resetInactivityTimer()
    }


}