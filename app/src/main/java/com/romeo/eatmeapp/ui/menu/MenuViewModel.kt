package com.romeo.eatmeapp.ui.menu

import androidx.lifecycle.ViewModel
import com.romeo.eatmeapp.data.model.CategoryModel
import com.romeo.eatmeapp.data.model.DishModel
import com.romeo.eatmeapp.data.model.SubCategoryModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MenuViewModel : ViewModel() {

    private val _categories = MutableStateFlow<List<CategoryModel>>(emptyList())
    val categories: StateFlow<List<CategoryModel>> = _categories.asStateFlow()

    private val _selectedCategory = MutableStateFlow<CategoryModel?>(null)
    val selectedCategory: StateFlow<CategoryModel?> = _selectedCategory.asStateFlow()

    private val _dishes = MutableStateFlow<List<DishModel>>(emptyList())
    val dishes: StateFlow<List<DishModel>> = _dishes.asStateFlow()

    private val _currentSubcategories = MutableStateFlow<List<SubCategoryModel>>(emptyList())
    val currentSubcategories: StateFlow<List<SubCategoryModel>> = _currentSubcategories

    private val _currentDishes = MutableStateFlow<List<DishModel>>(emptyList())
    val currentDishes: StateFlow<List<DishModel>> = _currentDishes

    fun setCategories(list: List<CategoryModel>) {
        _categories.value = list
        _selectedCategory.value = list.firstOrNull() // выберем первую по умолчанию
        updateDishesForCategory()
    }

    fun selectCategory(category: CategoryModel) {
        if (category.subcategories.isNotEmpty()) {
            _currentSubcategories.value = category.subcategories
            _currentDishes.value = emptyList()
        } else {
            _currentDishes.value = category.dishes
            _currentSubcategories.value = emptyList()
        }
    }

    fun selectSubcategory(subcategory: SubCategoryModel) {
        _currentDishes.value = subcategory.dishes
    }

    private fun updateDishesForCategory() {
        val category = _selectedCategory.value ?: return
        _dishes.value = fakeDishes().filter { it.category == category.name }
    }



    private fun fakeDishes(): List<DishModel> {
        return listOf(
            DishModel("1", "Espresso", "Coffee", "Strong", 100, "https://i.imgur.com/mRG0qWE.jpeg"),
            DishModel("2", "Latte", "Coffee", "Milk coffee", 150, "https://i.imgur.com/bXgKBot.jpeg"),
            DishModel("3", "Cola", "Soda", "Cold drink", 90, "https://i.imgur.com/N5MStEK.png"),
            DishModel("4", "Tea", "Tea", "Hot drink", 80, "https://i.imgur.com/KejyHye.png"),
        )
    }



}