package com.romeo.eatmeapp.adminpanel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romeo.eatmeapp.data.model.RestaurantModel
import com.romeo.eatmeapp.data.repository.RestaurantDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AdminActivityViewModel(
    private val repository: RestaurantDataSource
) : ViewModel() {

    private val _restaurant = MutableStateFlow<RestaurantModel?>(null)
    val restaurant: StateFlow<RestaurantModel?> = _restaurant.asStateFlow()

    fun loadRestaurant() {
        viewModelScope.launch {
            val data = repository.getRestaurantData()
            _restaurant.value = data
        }
    }
}