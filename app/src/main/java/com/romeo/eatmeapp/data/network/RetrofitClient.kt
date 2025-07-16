package com.romeo.eatmeapp.data.network

import com.google.gson.GsonBuilder
import com.romeo.eatmeapp.data.repository.MenuRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://6870c7737ca4d06b34b7f670.mockapi.io/api/data"

    private val gson = GsonBuilder().create()

    private val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()


    val api: ApiRetrofit = retrofit.create(ApiRetrofit::class.java)

    val menuRepository = MenuRepository(api)
}