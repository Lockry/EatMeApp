package com.romeo.eatmeapp.adminpanel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romeo.eatmeapp.RestaurantDataObject
import com.romeo.eatmeapp.data.model.RestaurantModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn


class AdminActivityViewModel : ViewModel() {

    //проксти на сингл тон
    val restaurant: StateFlow<RestaurantModel?> = RestaurantDataObject.restaurantModel

/* При неоходимости

    val locationName: StateFlow<String> = restaurant
        .map { it?.locationName ?: "—" }
        .stateIn(viewModelScope, SharingStarted.Eagerly, "—")

    val locatioId: StateFlow<String> = restaurant
        .map { it?.locationId ?: "—" }
        .stateIn(viewModelScope, SharingStarted.Eagerly, "—")

    val locatioAdress: StateFlow<String> = restaurant
        .map { it?.address ?: "—" }
        .stateIn(viewModelScope, SharingStarted.Eagerly, "—")

    val hasGameZone: StateFlow<Boolean> = restaurant
        .map { it?.hasGameZone ?: false }
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)
     
 */
}

