package com.romeo.eatmeapp.ui.mainmenu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romeo.eatmeapp.data.model.MainMenuModel
import com.romeo.eatmeapp.data.repository.MainMenuRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainMenuViewModel(
    private val itemMainMenu: MainMenuRepository
) : ViewModel() {

    private val _mainMenuItem = MutableStateFlow<List<MainMenuModel>>(emptyList())
    val mainMenuItem: StateFlow<List<MainMenuModel>> = _mainMenuItem

    private val _gameZone = MutableStateFlow(false)
    val gameZone: StateFlow<Boolean> = _gameZone

    fun loadMenu() {
        viewModelScope.launch {
            _mainMenuItem.value = itemMainMenu.getMainMenu()
        }
    }
}