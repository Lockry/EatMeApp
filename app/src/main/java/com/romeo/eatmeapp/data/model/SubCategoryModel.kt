package com.romeo.eatmeapp.data.model

data class SubCategoryModel(
    override val id: String = "",
    val name: String,
    val imageUri: String = "",
    val dishes: List<DishModel>
): HasId<String>