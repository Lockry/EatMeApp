package com.romeo.eatmeapp.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romeo.eatmeapp.data.model.SplashModel
import com.romeo.eatmeapp.data.repository.RestaurantDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.launch
import javax.inject.Inject

class SplashScreenViewModel @Inject constructor(
    private val repository: RestaurantDataSource
) : ViewModel() {

    private val _ads = MutableStateFlow<List<SplashModel>>(emptyList())
    val ads: StateFlow<List<SplashModel>> = _ads.asStateFlow()

    fun loadSplashScreens() {
        viewModelScope.launch {
            val restaurant = repository.getRestaurantData()
            _ads.value = restaurant.splashScreens
        }
    }

    fun loadSplashScreensFlow() {
        viewModelScope.launch {
            repository.getRestaurantDataFlow()
                .retry(3)
                .catch { e ->
                    _ads.value = emptyList()
                }
                .collect { restData ->
                    _ads.value = restData.firstOrNull()?.splashScreens ?: emptyList()
                }
        }
    }
}
