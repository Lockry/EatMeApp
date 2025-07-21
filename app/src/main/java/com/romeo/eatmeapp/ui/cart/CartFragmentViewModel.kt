package com.romeo.eatmeapp.ui.cart

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.romeo.eatmeapp.data.model.DishModel

class CartFragmentViewModel : ViewModel() {

    private val _dishesCart = MutableLiveData<List<DishModel>>()
    val dishesCart: LiveData<List<DishModel>> = _dishesCart

    val internalCartList = mutableListOf<DishModel>()


    fun loadCart() {
        _dishesCart.value = internalCartList.toList()
        Log.d("CartViewModel_funLoadCart", "Cart is : ${internalCartList}")
    }

    fun clearCart() {
        internalCartList.clear()
        _dishesCart.value = listOf()
    }

    fun getTotalPrice(): Int {
        return internalCartList.sumOf { it.price * it.quantity }
    }

    fun getTotalDishes(): Int {
        return internalCartList.sumOf { it.quantity  }
    }

    fun addToCart(item: DishModel) {
        // если уже есть = +1 кол-во
        val index = internalCartList.indexOfFirst { it.id == item.id }
        if (index >= 0) {
            val current = internalCartList[index]
            internalCartList[index] = current.copy(quantity = current.quantity + item.quantity)
        } else {
            internalCartList.add(item)
        }
        _dishesCart.value = internalCartList.toList()
        Log.d("DishBottomAddToCart", "${_dishesCart.value}")
    }

    fun onIncreaseQuantity(dishId: String) {
        val index = internalCartList.indexOfFirst { it.id == dishId }
        if (index >= 0) {
            val dish = internalCartList[index]
            internalCartList[index] = dish.copy(quantity = dish.quantity + 1)
            _dishesCart.value = internalCartList.toList()
        }
    }

    fun onDecreaseQuantity(dishId: String) {
        val index = internalCartList.indexOfFirst { it.id == dishId }
        if (index >= 0) {
            val dish = internalCartList[index]
            if (dish.quantity > 1) {
                internalCartList[index] = dish.copy(quantity = dish.quantity - 1)
                _dishesCart.value = internalCartList.toList()
            }
        }
    }

    fun onRemoveItem(position: Int) {
        internalCartList.removeAt(position)
        _dishesCart.value = internalCartList.toList()
    }


}