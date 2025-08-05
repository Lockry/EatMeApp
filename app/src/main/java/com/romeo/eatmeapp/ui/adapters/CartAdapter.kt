package com.romeo.eatmeapp.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.romeo.eatmeapp.R
import com.romeo.eatmeapp.data.model.DishModel
import com.romeo.eatmeapp.ui.adapters.diffcallback.BaseAnimatedDiffAdapter
import com.romeo.eatmeapp.ui.adapters.diffcallback.GenericDiffCallback
import com.romeo.eatmeapp.ui.cart.CartFragmentViewModel

class CartAdapter(
    private val listener: CartFragmentViewModel
) : BaseAnimatedDiffAdapter<DishModel, CartAdapter.CartViewHolder>(GenericDiffCallback()) {

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dishName: TextView = itemView.findViewById(R.id.textView_dish_name)
        val dishPrice: TextView = itemView.findViewById(R.id.textView_dish_price)
        val quantityText: TextView = itemView.findViewById(R.id.text_count)
        val plusButton: ImageButton = itemView.findViewById(R.id.btn_plus)
        val minusButton: ImageButton = itemView.findViewById(R.id.btn_minus)
        val deleteButton: ImageButton = itemView.findViewById(R.id.btn_remove_dish)
        val imageDish: ImageView = itemView.findViewById(R.id.image_dish)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        Log.d("CartAdapter", "Создаю ViewHolder")
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart_holder, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindView(holder: CartViewHolder, position: Int) {
        val dish = items[position]
        val imageUri = dish.imageUri.toUri()

        Log.d("CartAdapter", "Отрисовываю элемент: ${dish.name}")
        Glide.with(holder.itemView.context)
            .load(imageUri)
            .transition(DrawableTransitionOptions.withCrossFade(300))
            .into(holder.imageDish)

        holder.dishName.text = dish.name
        holder.dishPrice.text = "${dish.price * dish.quantity} ₽"
        holder.quantityText.text = dish.quantity.toString()

        holder.plusButton.setOnClickListener {
            listener.onIncreaseQuantity(dish.id)
        }

        holder.minusButton.setOnClickListener {
            listener.onDecreaseQuantity(dish.id)
        }

        holder.deleteButton.setOnClickListener {
            listener.onRemoveItem(position)
        }
    }


    override fun getItemCount(): Int {
        Log.d("CartAdapter", "getItemCount() = ${items.size}")
        return items.size
    }

    override fun getItemViewType(position: Int): Int = 0
}