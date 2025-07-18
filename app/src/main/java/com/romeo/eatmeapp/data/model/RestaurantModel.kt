package com.romeo.eatmeapp.data.model

import com.google.gson.annotations.SerializedName

data class RestaurantModel(
    @SerializedName("locationId") val locationId: String,
    @SerializedName("locationName") val locationName: String,
    @SerializedName("address") val address: String,
    @SerializedName("hasGameZone") val hasGameZone: Boolean,
    @SerializedName("menu") val menu: List<CategoryModel>,
    @SerializedName("splashScreens") val splashScreens: List<SplashModel>
)