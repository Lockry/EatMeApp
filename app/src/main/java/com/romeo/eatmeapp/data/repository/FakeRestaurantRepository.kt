package com.romeo.eatmeapp.data.repository

import android.content.Context
import com.google.gson.Gson
import com.romeo.eatmeapp.data.model.RestaurantModel
import javax.inject.Inject

class FakeRestaurantRepository @Inject constructor(
    private val context: Context
) : RestaurantDataSource {

    override suspend fun getRestaurantData(): RestaurantModel {
        val jsonString = context.assets.open("restaurant_test.json")
            .bufferedReader()
            .use { it.readText() }

        return Gson().fromJson(jsonString, RestaurantModel::class.java)
    }
}