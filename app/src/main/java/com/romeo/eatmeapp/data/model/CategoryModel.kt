package com.romeo.eatmeapp.data.model

data class CategoryModel(
    val id: String,
    val name: String,
    val imageUri: String = "",
    val subcategories: List<SubCategoryModel> = emptyList(),
    val dishes: List<DishModel> = emptyList()
)

data class SubCategoryModel(
    val id: String,
    val name: String,
    val imageUri: String = "",
    val dishes: List<DishModel>
)