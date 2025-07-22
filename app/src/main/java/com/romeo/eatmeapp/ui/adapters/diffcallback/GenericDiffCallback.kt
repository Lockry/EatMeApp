package com.romeo.eatmeapp.ui.adapters.diffcallback

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import com.romeo.eatmeapp.data.model.HasId

class GenericDiffCallback<T : HasId<*>> : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals") // data class -> .equals() true
    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem == newItem
    }
}