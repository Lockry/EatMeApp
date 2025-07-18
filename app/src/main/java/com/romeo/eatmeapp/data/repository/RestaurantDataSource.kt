package com.romeo.eatmeapp.data.repository

import com.romeo.eatmeapp.data.model.RestaurantModel
import com.romeo.eatmeapp.data.model.SplashModel

interface RestaurantDataSource {
    suspend fun getRestaurantData(): RestaurantModel
}
