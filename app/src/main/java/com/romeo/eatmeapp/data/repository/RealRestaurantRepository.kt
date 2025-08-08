package com.romeo.eatmeapp.data.repository

import android.util.Log
import com.romeo.eatmeapp.data.model.RestaurantModel
import com.romeo.eatmeapp.data.network.RestaurantApi
import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RealRestaurantRepository @Inject constructor(
    private val api: RestaurantApi, private val refreshTimer: Long
) : RestaurantDataSource {

    override suspend fun getRestaurantData(): RestaurantModel {
        return api.getRestaurantData().firstOrNull()
            ?: throw IllegalStateException("No data from API :(")
    }

    override fun getRestaurantDataFlow(): Flow<List<RestaurantModel>> = flow {
        while (true) {
            try {
                val result = api.getRestaurantData()
                emit(result)
            } catch (e: Exception) {
                Log.e("RetrofitAPI", "Error. Can't load from API:", e)
            }
            delay(refreshTimer)
            Log.i("RestRepoTimer", "RefreshTimer is: $refreshTimer")
        }
    }
}
