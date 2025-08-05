package com.romeo.eatmeapp.dagger

import com.romeo.eatmeapp.adminpanel.AdminActivity
import com.romeo.eatmeapp.data.repository.RestaurantDataSource
import com.romeo.eatmeapp.ui.menu.MenuFragment
import com.romeo.eatmeapp.ui.splash.SplashScreenFragment
import dagger.Component
import javax.inject.Named
import javax.inject.Singleton


@Component(
    modules = [
        AppModule::class,
        DataBaseModule::class,
        NetworkModule::class,
        RepositoryModule::class,
        ViewModelModule::class
    ]
)
@Singleton
interface AppComponent {

    fun inject(viewModel: MenuFragment)
    fun inject(viewModel: AdminActivity)
    fun inject(viewModel: SplashScreenFragment)

    @Named("fake")
    fun provideFakeRepository(): RestaurantDataSource

    @Named("real")
    fun provideRealRepository(): RestaurantDataSource

    fun provideRepository(): RestaurantDataSource
}

