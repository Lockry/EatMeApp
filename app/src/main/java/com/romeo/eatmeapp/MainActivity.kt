package com.romeo.eatmeapp

import android.os.Bundle
import android.view.MotionEvent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.romeo.eatmeapp.databinding.ActivityMainBinding
import com.romeo.eatmeapp.ui.mainmenu.MainMenuViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    var isTestMode = false

    private val timerSpashScreen = 10000L // 5 сек
    private var inactivityJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.nav_host_fragment)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        resetInactivityTimer()


    }


    private fun resetInactivityTimer() {
        inactivityJob?.cancel() // Отменяем предыдущую задачу

        inactivityJob = lifecycleScope.launch {
            delay( timerSpashScreen)
            // Переход на SplashFragment
            findNavController(R.id.nav_host_fragment).navigate(R.id.action_go_toSplashScreen)
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