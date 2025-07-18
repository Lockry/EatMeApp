package com.romeo.eatmeapp.data.repository

import com.romeo.eatmeapp.data.model.RestaurantModel
import com.romeo.eatmeapp.data.network.RestaurantApi

class RealRestaurantRepository(
    private val api: RestaurantApi
) : RestaurantDataSource {

    override suspend fun getRestaurantData(): RestaurantModel {
        return api.getRestaurantData().firstOrNull()
            ?: throw IllegalStateException("Нет данных ресторана")
    }

}
