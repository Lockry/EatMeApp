package com.romeo.eatmeapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.romeo.eatmeapp.R
import com.bumptech.glide.Glide
import com.romeo.eatmeapp.data.model.DishModel

class DishAdapter(
    private val onClick: (DishModel) -> Unit
) : RecyclerView.Adapter<DishAdapter.DishViewHolder>() {

    private var items = listOf<DishModel>()

    class DishViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView_dish)
        val desc: TextView = itemView.findViewById(R.id.desc_text_dish)
        val price: TextView = itemView.findViewById(R.id.dish_price)
        val btnAction: Button = itemView.findViewById(R.id.actionButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DishViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_dish_holder, parent, false)
        return DishViewHolder(view)
    }

    override fun onBindViewHolder(holder: DishViewHolder, position: Int) {
        val item = items[position]
        val imageUri = item.imageUri.toUri()

        Glide.with(holder.itemView.context)
            .load(imageUri)
            // .placeholder(R.drawable.placeholder)  // пока грузится
            // .error(R.drawable.error_image)        // если ошибка
            .into(holder.imageView)

        holder.desc.text = item.name
        holder.price.text = item.price.toString()

        holder.btnAction.setOnClickListener { onClick(item) }
    }

    override fun getItemCount(): Int = items.size

    fun setData(data: List<DishModel>) {
        items = data
        notifyDataSetChanged()
    }
}
