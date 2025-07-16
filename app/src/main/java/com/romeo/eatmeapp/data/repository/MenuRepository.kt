package com.romeo.eatmeapp.data.repository

import com.romeo.eatmeapp.data.model.CategoryModel
import com.romeo.eatmeapp.data.network.ApiRetrofit

class MenuRepository(private val api: ApiRetrofit) {

    suspend fun loadMenu(): List<CategoryModel> {
        return api.getRestaurantData().menu
    }

    suspend fun getLocationInfo(): String {
        val response = api.getRestaurantData()
        return "${response.locationName}, ${response.address}"
    }

    suspend fun hasGameZone(): Boolean {
        return api.getRestaurantData().hasGameZone
    }
}
