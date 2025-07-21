package com.romeo.eatmeapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.romeo.eatmeapp.ui.nointernet.NetworkObserver
import com.romeo.eatmeapp.ui.nointernet.NetworkStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

//AndroidViewModel специально предназначен для ViewModel, которым нужен Application.
class MainActivityViewModel(application: Application) :  AndroidViewModel(application) {

    private val networkObserver = NetworkObserver(application.applicationContext)

    private val _networkStatus = MutableStateFlow(networkObserver.getCurrentStatus())
    val networkStatus: StateFlow<NetworkStatus> = _networkStatus.asStateFlow()

    init {
        viewModelScope.launch {
            networkObserver.observe().collect {
                _networkStatus.value = it
            }
        }
    }
}
