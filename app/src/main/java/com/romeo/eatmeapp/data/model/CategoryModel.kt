package com.romeo.eatmeapp.data.model

data class CategoryModel(
    override val id: String = "",
    val name: String,
    val imageUri: String = "",
    val subcategories: List<SubCategoryModel> = emptyList(),
    val dishes: List<DishModel> = emptyList()
): HasId<String>

