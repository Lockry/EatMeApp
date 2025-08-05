package com.romeo.eatmeapp.data.repository

import com.romeo.eatmeapp.data.model.RestaurantModel
import com.romeo.eatmeapp.data.network.RestaurantApi
import javax.inject.Inject

class RealRestaurantRepository @Inject constructor(
    private val api: RestaurantApi
) : RestaurantDataSource {

    override suspend fun getRestaurantData(): RestaurantModel {
        return api.getRestaurantData().firstOrNull()
            ?: throw IllegalStateException("No data from API :(")
    }

}
