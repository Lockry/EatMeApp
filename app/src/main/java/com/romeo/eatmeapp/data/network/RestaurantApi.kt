package com.romeo.eatmeapp.data.network

import com.romeo.eatmeapp.data.model.RestaurantModel
import retrofit2.http.GET

interface RestaurantApi {
    @GET("data/RestaurantData")
    suspend fun getRestaurantData(): List<RestaurantModel>
}