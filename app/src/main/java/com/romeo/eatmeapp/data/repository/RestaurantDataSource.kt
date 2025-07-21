package com.romeo.eatmeapp.data.repository

import com.romeo.eatmeapp.data.model.RestaurantModel

interface RestaurantDataSource {
    suspend fun getRestaurantData(): RestaurantModel
}
