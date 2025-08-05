package com.romeo.eatmeapp.dagger

import android.content.Context
import com.romeo.eatmeapp.data.network.RestaurantApi
import com.romeo.eatmeapp.data.repository.FakeRestaurantRepository
import com.romeo.eatmeapp.data.repository.RealRestaurantRepository
import com.romeo.eatmeapp.data.repository.RestaurantDataSource
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
class RepositoryModule(private val isTestMode: Boolean) {

    @Provides
    @Singleton
    @Named("fake")
    fun provideFakeRepository(context: Context): RestaurantDataSource {
        return FakeRestaurantRepository(context)
    }

    @Provides

    @Named("real")
    fun provideRealRepository(api: RestaurantApi): RestaurantDataSource {
        return RealRestaurantRepository(api)
    }

    @Provides
    @Singleton
    fun provideRepository(context: Context, api: RestaurantApi): RestaurantDataSource {
        return if (isTestMode) {
            FakeRestaurantRepository(context)
        } else {
            RealRestaurantRepository(api)
        }
    }

}