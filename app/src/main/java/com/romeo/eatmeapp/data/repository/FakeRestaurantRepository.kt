package com.romeo.eatmeapp.data.repository

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.romeo.eatmeapp.data.model.RestaurantModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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

    override fun getRestaurantDataFlow(): Flow<List<RestaurantModel>> = flow {

        try {
            val jsonString = context.assets.open("restaurant_test.json")
                .bufferedReader()
                .use { it.readText() }

            val restarauntModel = Gson().fromJson(jsonString, RestaurantModel::class.java)
            emit(listOf(restarauntModel))
        }catch (e: Exception){
            emit(emptyList())
            Log.e("FakeRestaurantRepository", "No data from JSON:", e)
        }

    }
}