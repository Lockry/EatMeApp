package com.romeo.eatmeapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.romeo.eatmeapp.R
import com.romeo.eatmeapp.data.model.CategoryModel
import com.romeo.eatmeapp.ui.adapters.CategoryAdapter.CategoryViewHolder
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView

class CategoryAdapter(
    private val onClick: (CategoryModel) -> Unit
) : RecyclerView.Adapter<CategoryViewHolder>() {

    private var items = listOf<CategoryModel>()
    private var selectedPosition = -1 // Для отслеживания выбранного элемента

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ShapeableImageView = itemView.findViewById(R.id.image_category)
        val descTextView: TextView = itemView.findViewById(R.id.desc_text_category)
        val containerLayout: LinearLayout = itemView.findViewById(R.id.holder_category)
        val cardView: androidx.cardview.widget.CardView = itemView as androidx.cardview.widget.CardView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category_holder, parent, false)

        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val item = items[position]

        val imageUri = item.imageUri.toUri()
        val isSelected = position == selectedPosition

        Glide.with(holder.itemView.context)
            .load(imageUri)
//            .placeholder(R.drawable.placeholder_image) // пока грузим
//            .error(R.drawable.error_image) // Ошибка еди не загружено
            .into(holder.imageView)

        holder.descTextView.text = item.name
        holder.cardView.isSelected = isSelected

        holder.descTextView.setTextColor(
            ContextCompat.getColor(holder.itemView.context,
                if (isSelected) android.R.color.white else android.R.color.black
            )
        )

        holder.containerLayout.setOnClickListener {
            val pos = holder.adapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                selectedPosition = pos
                notifyDataSetChanged()
                onClick(items[pos])
            }
        }
    }

    override fun getItemCount(): Int = items.size

    fun setData(data: List<CategoryModel>, autoSelectFirst: Boolean = true) {
        items = data

        if (autoSelectFirst && data.isNotEmpty()) {
            selectedPosition = 0
            notifyDataSetChanged()
            onClick(data[0]) // прокидываем первый айтем
            
        } else {
            notifyDataSetChanged()
        }
    }
}