package com.romeo.eatmeapp.ui.mainmenu

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CustomLayoutManager(context: Context) : LinearLayoutManager(context) {
    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        super.onLayoutChildren(recycler, state)

        // принудительная высота каждой плитке
        val parentHeight = height - 50
        val itemCount = itemCount.coerceAtLeast(1)

        val itemHeight = parentHeight / itemCount

        for (i in 0 until childCount) {
            val child = getChildAt(i)
            child?.layoutParams?.height = itemHeight
            child?.requestLayout()
        }
    }

    override fun canScrollVertically(): Boolean = false
}