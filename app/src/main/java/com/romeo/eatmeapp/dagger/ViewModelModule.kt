package com.romeo.eatmeapp.dagger

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.romeo.eatmeapp.adminpanel.AdminActivityViewModel
import com.romeo.eatmeapp.ui.menu.MenuViewModel
import com.romeo.eatmeapp.ui.splash.SplashScreenViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: DaggerViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MenuViewModel::class)
    abstract fun bindMenuViewModel(menuViewModel: MenuViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SplashScreenViewModel::class)
    abstract fun bindSplashScreenViewModel(splashScreenViewModel: SplashScreenViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AdminActivityViewModel::class)
    abstract fun bindAdminActivityViewModel(adminActivityViewModel: AdminActivityViewModel): ViewModel
}