package com.romeo.eatmeapp.ui.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romeo.eatmeapp.data.model.CategoryModel
import com.romeo.eatmeapp.data.model.DishModel
import com.romeo.eatmeapp.data.model.MenuItemModel
import com.romeo.eatmeapp.data.model.SubCategoryModel
import com.romeo.eatmeapp.data.repository.MenuDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn


class MenuViewModel(private val repository: MenuDataSource) : ViewModel() {

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


    fun setMenu(list: List<CategoryModel>) {
        _categories.value = list
        _selectedCategory.value = list.firstOrNull()
    }

    fun selectCategory(category: CategoryModel) {
        _currentSubcategories.value = if (category.subcategories.isNotEmpty()) {
            category.subcategories
        } else {
            emptyList()
        }

        _currentDishes.value = if (category.subcategories.isEmpty()) {
            category.dishes
        } else {
            emptyList()
        }
    }


    fun selectSubcategory(subcategory: SubCategoryModel) {
        _currentDishes.value = subcategory.dishes
        _currentSubcategories.value = emptyList()
    }


    val currentMenuItems: StateFlow<List<MenuItemModel>> = combine(
        _currentSubcategories,
        _currentDishes
    ) { subcategories, dishes ->
        if (subcategories.isNotEmpty()) {
            subcategories.map { MenuItemModel.SubCategoryItem(it) }
        } else if (dishes.isNotEmpty()) {
            dishes.map { MenuItemModel.DishItem(it) }
        } else {
            emptyList()
        }
    }.stateIn(   // flow->stateFlow
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
        /*
        scope — жизненный цикл, в котором будет работать этот поток (например, viewModelScope).
        started — политика запуска (SharingStarted.WhileSubscribed — самый частый вариант).
        initialValue — начальное значение, пока данные не пришли.
         */
    )

}