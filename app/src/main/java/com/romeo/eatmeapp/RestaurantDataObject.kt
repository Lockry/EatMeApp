package com.romeo.eatmeapp

import android.util.Log
import com.romeo.eatmeapp.data.model.RestaurantModel
import com.romeo.eatmeapp.data.repository.RestaurantDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object RestaurantDataObject {

    private lateinit var repository: RestaurantDataSource

    private val _restaurantModel = MutableStateFlow<RestaurantModel?>(null)
    val restaurantModel = _restaurantModel.asStateFlow()

    suspend fun init(repository: RestaurantDataSource) {
        this.repository = repository
        val data = repository.getRestaurantData()
        _restaurantModel.value = data
    }

    fun hasGameZone(): Boolean {
        return _restaurantModel.value?.hasGameZone == true
    }

    suspend fun forceReload(repository: RestaurantDataSource) {
        this.repository = repository
        val data = repository.getRestaurantData()
        Log.d("[RestaurantData]", "Reloaded data: $data")
        _restaurantModel.value = data
    }
}
