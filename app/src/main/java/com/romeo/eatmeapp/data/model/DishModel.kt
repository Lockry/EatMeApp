package com.romeo.eatmeapp.data.model
import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class DishModel(
    val id: String = "",
    var name: String = "",
    val category: String = "",
    var desc: String = "",
    var price: Int = 0,
    var imageUri: String = "",
    var quantity: Int = 1
) : Parcelable