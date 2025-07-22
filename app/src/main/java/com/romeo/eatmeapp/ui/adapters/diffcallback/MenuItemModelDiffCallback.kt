package com.romeo.eatmeapp.ui.adapters.diffcallback

import androidx.recyclerview.widget.DiffUtil
import com.romeo.eatmeapp.data.model.MenuItemModel


class MenuItemModelDiffCallback : DiffUtil.ItemCallback<MenuItemModel>() {
    override fun areItemsTheSame(oldItem: MenuItemModel, newItem: MenuItemModel): Boolean {
        return when {
            oldItem is MenuItemModel.DishItem && newItem is MenuItemModel.DishItem ->
                oldItem.dish.id == newItem.dish.id
            oldItem is MenuItemModel.SubCategoryItem && newItem is MenuItemModel.SubCategoryItem ->
                oldItem.subCategory.id == newItem.subCategory.id
            else -> oldItem == newItem
        }
    }

    override fun areContentsTheSame(oldItem: MenuItemModel, newItem: MenuItemModel): Boolean {
        return oldItem == newItem
    }
}