package com.romeo.eatmeapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.imageview.ShapeableImageView
import com.romeo.eatmeapp.R
import com.romeo.eatmeapp.data.model.CategoryModel
import com.romeo.eatmeapp.ui.adapters.diffcallback.BaseAnimatedDiffAdapter
import com.romeo.eatmeapp.ui.adapters.diffcallback.GenericDiffCallback

class CategoryAdapter(
    private val onClick: (CategoryModel) -> Unit
) : BaseAnimatedDiffAdapter<CategoryModel, CategoryAdapter.CategoryViewHolder>(GenericDiffCallback()) {

    private var selectedPosition = -1
        set(value) {
            val oldValue = field
            field = value
            notifyItemChanged(oldValue)
            notifyItemChanged(value)
        }



    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ShapeableImageView = itemView.findViewById(R.id.image_category)
        val descTextView: TextView = itemView.findViewById(R.id.desc_text_category)
        val containerLayout: LinearLayout = itemView.findViewById(R.id.holder_category)
        val cardView: CardView = itemView as CardView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category_holder, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindView(holder: CategoryViewHolder, position: Int) {
        val item = items[position]
        val isSelected = position == selectedPosition

        val imageUri = item.imageUri.toUri()
        Glide.with(holder.itemView.context)
            .load(imageUri)
            .transition(DrawableTransitionOptions.withCrossFade(300))
            .into(holder.imageView)

        holder.descTextView.text = item.name
        holder.cardView.isSelected = isSelected

        holder.descTextView.setTextColor(
            ContextCompat.getColor(
                holder.itemView.context,
                if (isSelected) android.R.color.white else android.R.color.black
            )
        )

        holder.containerLayout.setOnClickListener {
            val pos = holder.adapterPosition
            if (pos != RecyclerView.NO_POSITION && pos != selectedPosition) {
                selectedPosition = pos
                onClick(item)
            }
        }
    }

    fun selectFirstIfEmpty() {
        if (selectedPosition == -1 && items.isNotEmpty()) {
            selectedPosition = 0
        }
    }

    override fun getItemViewType(position: Int): Int = 0
}