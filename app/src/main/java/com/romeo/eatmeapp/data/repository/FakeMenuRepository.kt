package com.romeo.eatmeapp.data.repository

import android.content.Context
import com.google.gson.Gson
import com.romeo.eatmeapp.data.model.CategoryModel
import com.romeo.eatmeapp.data.model.RestaurantModel

class FakeMenuRepository(private val context: Context): MenuDataSource  {




    fun getLocationInfo(): String {
        val jsonString = context.assets.open("restaurant_data.json")
            .bufferedReader()
            .use { it.readText() }

        val data = Gson().fromJson(jsonString, RestaurantModel::class.java)
        return "${data.locationName}, ${data.address}"
    }

    fun hasGameZone(): Boolean {
        val jsonString = context.assets.open("restaurant_data.json")
            .bufferedReader()
            .use { it.readText() }

        val data = Gson().fromJson(jsonString, RestaurantModel::class.java)
        return data.hasGameZone
    }


    override suspend fun getRestaurantData(): RestaurantModel {
        TODO("Not yet implemented")
    }
}

