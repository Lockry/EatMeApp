package com.romeo.eatmeapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.romeo.eatmeapp.R
import com.romeo.eatmeapp.data.model.SubCategoryModel

class SubcategoryAdapter(
    private val onClick: (SubCategoryModel) -> Unit
) : RecyclerView.Adapter<SubcategoryAdapter.SubcategoryViewHolder>() {

    private var items = listOf<SubCategoryModel>()

    class SubcategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val desc: TextView = itemView.findViewById(R.id.desc_text_subcategory)
        val image: ImageView = itemView.findViewById(R.id.imageView_subcategory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubcategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_subcategory_holder, parent, false)
        return SubcategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: SubcategoryViewHolder, position: Int) {
        val item = items[position]
        val imageUri = item.imageUri.toUri()


        Glide.with(holder.itemView.context)
            .load(imageUri)
            // .placeholder(R.drawable.placeholder)  // пока грузится
            // .error(R.drawable.error_image)        // если ошибка
            .into(holder.image)

        holder.desc.text = item.name
        holder.itemView.setOnClickListener { onClick(item) }
    }

    override fun getItemCount(): Int = items.size

}
