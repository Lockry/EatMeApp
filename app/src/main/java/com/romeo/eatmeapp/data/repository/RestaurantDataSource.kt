package com.romeo.eatmeapp.data.repository

import com.romeo.eatmeapp.data.model.RestaurantModel
import kotlinx.coroutines.flow.Flow


interface RestaurantDataSource {
    suspend fun getRestaurantData(): RestaurantModel
    fun getRestaurantDataFlow(): Flow<List<RestaurantModel>>
}
