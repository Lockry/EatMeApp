package com.romeo.eatmeapp.data.repository

import com.romeo.eatmeapp.data.model.RestaurantModel

interface MenuDataSource {
    suspend fun getRestaurantData(): RestaurantModel
}
