package com.romeo.eatmeapp.adminpanel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.romeo.eatmeapp.data.repository.RestaurantDataSource

class AdminActivityViewModelFactory(
    private val repository: RestaurantDataSource
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AdminActivityViewModel::class.java)) {
            return AdminActivityViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}