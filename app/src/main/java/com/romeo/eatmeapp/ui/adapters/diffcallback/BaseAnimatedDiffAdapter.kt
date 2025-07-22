package com.romeo.eatmeapp.ui.adapters.diffcallback

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator

abstract class BaseAnimatedDiffAdapter<T : Any, VH : RecyclerView.ViewHolder>(
    private val diffCallback: DiffUtil.ItemCallback<T>
) : RecyclerView.Adapter<VH>() {

    var items: List<T> = emptyList()
        set(value) {
            if (field == value) return

            val diffResult = DiffUtil.calculateDiff(
                object : DiffUtil.Callback() {
                    override fun getOldListSize(): Int = field.size
                    override fun getNewListSize(): Int = value.size
                    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                        diffCallback.areItemsTheSame(field[oldItemPosition], value[newItemPosition])
                    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                        diffCallback.areContentsTheSame(field[oldItemPosition], value[newItemPosition])
                }
            )

            field = value
            diffResult.dispatchUpdatesTo(this)
        }

    private var lastAnimatedPosition = -1

    override fun getItemCount(): Int = items.size

    abstract override fun getItemViewType(position: Int): Int
    abstract override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH
    abstract fun onBindView(holder: VH, position: Int)

    override fun onBindViewHolder(holder: VH, position: Int) {
        onBindView(holder, position)
        runEnterAnimation(holder.itemView, position)
    }

    private fun runEnterAnimation(view: View, position: Int) {
        if (position > lastAnimatedPosition) {
            view.animate()
                .alpha(1f)
                .setDuration(200)
                .setStartDelay((position - lastAnimatedPosition) * 50L)
                .setInterpolator(DecelerateInterpolator(1.2f))
                .start()
        }
    }

}