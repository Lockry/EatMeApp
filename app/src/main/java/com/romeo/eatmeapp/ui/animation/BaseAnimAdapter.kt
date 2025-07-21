package com.romeo.eatmeapp.ui.animation

import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAnimAdapter<T> : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var isAnimated = true
    private var lastPosition = -1

    abstract fun onBindView(holder: RecyclerView.ViewHolder, position: Int)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        onBindView(holder, position)
        runEnterAnimation(holder.itemView, position)
    }

    private fun runEnterAnimation(view: View, position: Int) {
        if (!isAnimated) return
        if (position > lastPosition) {
            view.alpha = 0f
            view.translationY = 30f
            view.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(200)
                .setStartDelay((position - lastPosition) * 50L)
                .setInterpolator(DecelerateInterpolator(1.2f))
                .withEndAction {
                    lastPosition = position
                }
                .start()
        }
    }

    fun resetAnimation() {
        lastPosition = -1
    }

    abstract override fun getItemCount(): Int
    abstract override fun getItemViewType(position: Int): Int
    abstract override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
}