package com.romeo.eatmeapp.ui.mainmenu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romeo.eatmeapp.data.repository.RestaurantDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainMenuViewModel(
    private val repository: RestaurantDataSource
) : ViewModel() {

    private val _gameZone = MutableStateFlow(false)
    val gameZone: StateFlow<Boolean> = _gameZone.asStateFlow()

    fun loadMainMenu() {
        viewModelScope.launch {
            val restaurantData = repository.getRestaurantData()
            val hasGameZone = restaurantData.hasGameZone
            _gameZone.value = hasGameZone
        }
    }
}