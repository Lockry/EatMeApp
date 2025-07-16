package com.romeo.eatmeapp.data.model


sealed class MenuItemModel {
    abstract fun getItemId(): String

    data class SubCategoryItem(val subCategory: SubCategoryModel) : MenuItemModel() {
        override fun getItemId(): String = "sub_${subCategory.id}"
    }

    data class DishItem(val dish: DishModel) : MenuItemModel() {
        override fun getItemId(): String = "dish_${dish.id}"
    }
}
