package com.romeo.eatmeapp.ui.adapters

import android.annotation.SuppressLint
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
import com.romeo.eatmeapp.R
import com.romeo.eatmeapp.data.model.DishModel
import com.romeo.eatmeapp.ui.cart.CartFragmentViewModel

class CartAdapter(
    private val listener: CartFragmentViewModel
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    private var cartItems: List<DishModel> = emptyList()

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

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val dish = cartItems[position]
        val imageUri = dish.imageUri.toUri()

        Log.d("CartAdapter", "Отрисовываю элемент: ${dish.name}")
        Glide.with(holder.itemView.context)
            .load(imageUri)
            // .placeholder(R.drawable.placeholder)  // пока грузится
            // .error(R.drawable.error_image)        // если ошибка
            .into(holder.imageDish)                      // куда загружаем

        holder.dishName.text = dish.name
        holder.dishPrice.text = "${dish.price * dish.quantity} BYN"
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

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newItems: List<DishModel>) {
        Log.d("CartAdapter", "submitList called with size = ${newItems.size}")
        cartItems = newItems
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        Log.d("CartAdapter", "getItemCount() = ${cartItems.size}")
        return cartItems.size
    }
}