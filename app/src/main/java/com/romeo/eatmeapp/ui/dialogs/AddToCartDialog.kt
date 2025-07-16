package com.romeo.eatmeapp.ui.dialogs


import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.romeo.eatmeapp.data.model.DishModel
import com.romeo.eatmeapp.databinding.DialogAddToCartBinding

class AddToCartDialog(
    private val context: Context
) {
    private val binding = DialogAddToCartBinding.inflate(LayoutInflater.from(context))
    private val dialog = AlertDialog.Builder(context)
        .setView(binding.root)
        .create()

    private var quantity = 1
    private var currentDish: DishModel? = null

    init {
        setupListeners()

        dialog.setOnShowListener {
            dialog.window?.setLayout(
                (context.resources.displayMetrics.widthPixels * 0.95).toInt(),
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
    }

    private fun setupListeners() {
        binding.btnPlus.setOnClickListener {
            quantity++
            update()
        }

        binding.btnMinus.setOnClickListener {
            if (quantity > 1) {
                quantity--
                update()
            }
        }

        binding.btnAddToCart.setOnClickListener {

        }
    }

    private fun update() {
        binding.textCount.text = quantity.toString()
        binding.textViewDishPrice.text = currentDish?.price.toString()
    }

    fun show(dish: DishModel) {
        currentDish = dish
        quantity = 1

        Glide.with(context)
            .load(dish.imageUri)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(binding.imageDish)

        binding.textDishName.text = dish.name
        binding.textDishDesc.text = dish.desc

        update()
        dialog.show()
    }
}
