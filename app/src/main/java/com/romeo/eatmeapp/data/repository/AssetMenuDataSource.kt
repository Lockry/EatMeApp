package com.romeo.eatmeapp.data.repository

import android.content.Context
import com.google.gson.Gson
import com.romeo.eatmeapp.data.model.RestaurantModel

object FakeRestaurantLoader {
    fun loadFakeRestaurant(context: Context): RestaurantModel {
        val json = context.assets.open("restaurant_test.json")
            .bufferedReader()
            .use { it.readText() }

        val gson = Gson()
        return gson.fromJson(json, RestaurantModel::class.java)
    }
}
