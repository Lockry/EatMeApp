package com.romeo.eatmeapp.ui.menu

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romeo.eatmeapp.data.model.CategoryModel
import com.romeo.eatmeapp.data.model.DishModel
import com.romeo.eatmeapp.data.model.MenuItemModel
import com.romeo.eatmeapp.data.model.RestaurantModel
import com.romeo.eatmeapp.data.model.SubCategoryModel
import com.romeo.eatmeapp.data.repository.RestaurantDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


class MenuViewModel @Inject constructor(
    private val repository: RestaurantDataSource
) : ViewModel() {

    private val _categories = MutableStateFlow<List<CategoryModel>>(emptyList())
    val categories: StateFlow<List<CategoryModel>> = _categories.asStateFlow()

    private val _currentSubcategories = MutableStateFlow<List<SubCategoryModel>>(emptyList())

    private val _currentDishes = MutableStateFlow<List<DishModel>>(emptyList())

    private val _selectedCategory = MutableStateFlow<CategoryModel?>(null)
    val selectedCategory: StateFlow<CategoryModel?> = _selectedCategory.asStateFlow()

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


    fun loadMenuFlow(){
        viewModelScope.launch {
            repository.getRestaurantDataFlow()
                .retry(3)
                .catch { e ->
                    Log.e("MenuVM", "No data form FLOW:", e)
                    _selectedCategory.value = null
                    _currentSubcategories.value = emptyList()
                    _currentDishes.value = emptyList()
                }
                .collect {restData ->
                    val menu = restData.firstOrNull()?.menu ?: emptyList()
                    _categories.value = menu
                    val firstCategory = menu.firstOrNull()
                    firstCategory?.let { selectCategory(it)}
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
