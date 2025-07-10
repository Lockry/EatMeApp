package com.romeo.eatmeapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.romeo.eatmeapp.R
import com.romeo.eatmeapp.data.model.CategoryModel
import com.bumptech.glide.Glide

class CategoryAdapter(
    private val onClick: (CategoryModel) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    private var items = listOf<CategoryModel>()

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.image_category)
        val desc: TextView = itemView.findViewById(R.id.desc_text_category)
        val holder: LinearLayout = itemView.findViewById(R.id.holder_category)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category_holder, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val item = items[position]
        val imageUri = item.imageUri.toUri()

        Glide.with(holder.itemView.context)
            .load(imageUri)
            // .placeholder(R.drawable.placeholder)  // пока грузится
            // .error(R.drawable.error_image)        // если ошибка
            .into(holder.imageView)

        holder.desc.text = item.name
        holder.holder.setOnClickListener { onClick(item) }
    }

    override fun getItemCount(): Int = items.size

    fun setData(data: List<CategoryModel>) {
        items = data
        notifyDataSetChanged()
    }
}
