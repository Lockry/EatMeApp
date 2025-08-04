package com.romeo.eatmeapp

import android.app.Application
import android.util.Log
import com.romeo.eatmeapp.dagger.AppComponent
import com.romeo.eatmeapp.dagger.AppModule
import com.romeo.eatmeapp.dagger.DaggerAppComponent
import com.romeo.eatmeapp.dagger.NetworkModule
import com.romeo.eatmeapp.dagger.RepositoryModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AppApplication : Application() {

    private val prefs by lazy { getSharedPreferences("app_prefs", MODE_PRIVATE) }

    var isTestMode = true

    lateinit var appComponent: AppComponent
        private set


    override fun onCreate() {
        super.onCreate()

        isTestMode = prefs.getBoolean("is_test_mode", false)

        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .networkModule(NetworkModule)
            .repositoryModule(RepositoryModule(isTestMode))
            .build()

        initializeRestaurantData()
    }

    fun recreateAppComponent() {
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .repositoryModule(RepositoryModule(isTestMode = getTestModeFromPrefs()))
            .build()
    }

    private fun initializeRestaurantData() {
        // Запускаем инициализацию в фоне
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repository = if (isTestMode) {
                    appComponent.provideFakeRepository()
                } else {
                    appComponent.provideRealRepository()
                }
                RestaurantDataObject.init(repository)
                Log.d("AppApplication", "RestaurantDataObject initialized successfully")
            } catch (e: Exception) {
                Log.e("AppApplication", "Failed to initialize RestaurantDataObject", e)
            }
        }
    }

    fun getTestModeFromPrefs() = prefs.getBoolean("is_test_mode", false)
}