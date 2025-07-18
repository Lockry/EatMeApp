package com.romeo.eatmeapp.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SplashModel(
    val id: String = "",
    var name: String = "",
    var imageUri: String = ""
) : Parcelable