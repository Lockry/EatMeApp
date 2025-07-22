package com.romeo.eatmeapp.ui.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.romeo.eatmeapp.R
import com.romeo.eatmeapp.data.model.DishModel
import com.romeo.eatmeapp.data.model.MenuItemModel
import com.romeo.eatmeapp.data.model.SubCategoryModel
import com.romeo.eatmeapp.ui.adapters.diffcallback.BaseAnimatedDiffAdapter
import com.romeo.eatmeapp.ui.adapters.diffcallback.MenuItemModelDiffCallback

class MenuAdapter(
    private val onDishClick: (DishModel) -> Unit,
    private val onSubcategoryClick: (SubCategoryModel) -> Unit
) : BaseAnimatedDiffAdapter<MenuItemModel, RecyclerView.ViewHolder>(MenuItemModelDiffCallback()) {

    companion object {
        private const val VIEW_TYPE_DISH = 1
        private const val VIEW_TYPE_SUBCATEGORY = 2
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is MenuItemModel.DishItem -> VIEW_TYPE_DISH
            is MenuItemModel.SubCategoryItem -> VIEW_TYPE_SUBCATEGORY
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_DISH -> DishViewHolder(inflater.inflate(R.layout.item_dish_holder, parent, false))
            VIEW_TYPE_SUBCATEGORY -> SubcategoryViewHolder(inflater.inflate(R.layout.item_subcategory_holder, parent, false))
            else -> throw IllegalArgumentException("Unknown viewType $viewType")
        }
    }

    override fun onBindView(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        when (item) {
            is MenuItemModel.DishItem -> (holder as DishViewHolder).bind(item.dish)
            is MenuItemModel.SubCategoryItem -> (holder as SubcategoryViewHolder).bind(item.subCategory)
        }
    }

    inner class DishViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val image: ImageView = itemView.findViewById(R.id.imageView_dish)
        private val desc: TextView = itemView.findViewById(R.id.desc_text_dish)
        private val price: TextView = itemView.findViewById(R.id.dish_price)
        private val button: Button = itemView.findViewById(R.id.actionButton)

        fun bind(dish: DishModel) {
            desc.text = dish.name
            price.text = "${dish.price} ₽"

            val imageUri = dish.imageUri.toUri()
            Glide.with(itemView.context)
                .load(imageUri)
                .transition(DrawableTransitionOptions.withCrossFade(300))
                .into(image)

            button.setOnClickListener { onDishClick(dish) }
        }
    }

    inner class SubcategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val image: ImageView = itemView.findViewById(R.id.imageView_subcategory)
        private val desc: TextView = itemView.findViewById(R.id.desc_text_subcategory)

        fun bind(subCategory: SubCategoryModel) {
            desc.text = subCategory.name

            val imageUri = subCategory.imageUri.toUri()
            Glide.with(itemView.context)
                .load(imageUri)
                .transition(DrawableTransitionOptions.withCrossFade(300))
                .into(image)

            itemView.setOnClickListener { onSubcategoryClick(subCategory) }
        }
    }
}
