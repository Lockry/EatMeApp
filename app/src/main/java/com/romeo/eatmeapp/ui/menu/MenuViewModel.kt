package com.romeo.eatmeapp.ui.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romeo.eatmeapp.data.model.CategoryModel
import com.romeo.eatmeapp.data.model.DishModel
import com.romeo.eatmeapp.data.model.MenuItemModel
import com.romeo.eatmeapp.data.model.SubCategoryModel
import com.romeo.eatmeapp.data.repository.RestaurantDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class MenuViewModel(
    private val repository: RestaurantDataSource
) : ViewModel() {

    private val _categories = MutableStateFlow<List<CategoryModel>>(emptyList())
    val categories: StateFlow<List<CategoryModel>> = _categories.asStateFlow()

    private val _selectedCategory = MutableStateFlow<CategoryModel?>(null)

    private val _currentSubcategories = MutableStateFlow<List<SubCategoryModel>>(emptyList())

    private val _currentDishes = MutableStateFlow<List<DishModel>>(emptyList())

    val currentMenuItems: StateFlow<List<MenuItemModel>> = combine(
        _currentSubcategories,
        _currentDishes
    ) { subcategories, dishes ->
        when {
            subcategories.isNotEmpty() -> subcategories.map { MenuItemModel.SubCategoryItem(it) }
            dishes.isNotEmpty() -> dishes.map { MenuItemModel.DishItem(it) }
            else -> emptyList()
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    // Загрузка меню из репозитория
    fun loadMenu() {
        viewModelScope.launch {
            try {
                val restaurantData = repository.getRestaurantData()
                val menu = restaurantData.menu
                _categories.value = menu
                _selectedCategory.value = menu.firstOrNull()
                menu.firstOrNull()?.let {
                    selectCategory(it)
                }
            } catch (e: Exception) {
                // TODO: Обработка ошибок
                _categories.value = emptyList()
                _selectedCategory.value = null
                _currentSubcategories.value = emptyList()
                _currentDishes.value = emptyList()
            }
        }
    }

    fun selectCategory(category: CategoryModel) {
        _selectedCategory.value = category
        if (category.subcategories.isNotEmpty()) {
            _currentSubcategories.value = category.subcategories
            _currentDishes.value = emptyList()
        } else {
            _currentSubcategories.value = emptyList()
            _currentDishes.value = category.dishes
        }
    }

    fun selectSubcategory(subcategory: SubCategoryModel) {
        _currentDishes.value = subcategory.dishes
        _currentSubcategories.value = emptyList()
    }
}
