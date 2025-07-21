package com.romeo.eatmeapp.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.romeo.eatmeapp.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.imageview.ShapeableImageView
import com.romeo.eatmeapp.data.model.CategoryModel
import com.romeo.eatmeapp.ui.animation.BaseAnimAdapter

class CategoryAdapter(
    private val onClick: (CategoryModel) -> Unit
) : BaseAnimAdapter<CategoryModel>() {

    private var items = listOf<CategoryModel>()
    private var selectedPosition = -1

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ShapeableImageView = itemView.findViewById(R.id.image_category)
        val descTextView: TextView = itemView.findViewById(R.id.desc_text_category)
        val containerLayout: LinearLayout = itemView.findViewById(R.id.holder_category)
        val cardView: CardView = itemView as CardView
    }

    override fun getItemViewType(position: Int): Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category_holder, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindView(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        val isSelected = position == selectedPosition

        val imageUri = item.imageUri.toUri()
        Glide.with(holder.itemView.context)
            .load(imageUri)
            .transition(DrawableTransitionOptions.withCrossFade(300))
            .into((holder as CategoryViewHolder).imageView)

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

    @SuppressLint("NotifyDataSetChanged")
    fun setData(items: List<CategoryModel>, autoSelectFirst: Boolean = true) {
        this.items = items

        if (autoSelectFirst && items.isNotEmpty()) {
            selectedPosition = 0
            notifyDataSetChanged()
            resetAnimation()
            onClick(items[0])
        } else {
            resetAnimation()
            notifyDataSetChanged()
        }
    }
}