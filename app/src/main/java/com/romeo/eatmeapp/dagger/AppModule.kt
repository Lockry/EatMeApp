package com.romeo.eatmeapp.dagger

import android.content.Context
import com.romeo.eatmeapp.AppApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val context: Context) {

    @Provides
    @Singleton
    fun provideContext(): Context = context


    @Provides
    fun provideRefreshTimer(): Long = (context as AppApplication).refreshDataTimer
}