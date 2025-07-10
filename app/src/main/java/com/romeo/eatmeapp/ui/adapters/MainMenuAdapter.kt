package com.romeo.eatmeapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.romeo.eatmeapp.R
import com.romeo.eatmeapp.data.model.MainMenuModel


class MainMenuAdapter(
    private val onClick: (MainMenuModel) -> Unit
) : RecyclerView.Adapter<MainMenuAdapter.MenuViewHolder>() {

    private var items: List<MainMenuModel> = emptyList()

    class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val btnAction: Button = itemView.findViewById(R.id.btnActionMenu)
        val image: ImageView = itemView.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_main_menu_holder, parent, false)
        return MenuViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val mainMenu = items[position]
        val imageUri = mainMenu.imageUri.toUri()

        Glide.with(holder.itemView.context)
            .load(imageUri)
            // .placeholder(R.drawable.placeholder)  // пока грузится
            // .error(R.drawable.error_image)        // если ошибка
            .into(holder.image)

        holder.btnAction.text = mainMenu.name

        holder.btnAction.setOnClickListener {
            onClick(mainMenu)
        }

        holder.itemView.setOnClickListener {
            onClick(mainMenu)
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateData(newItems: List<MainMenuModel>) {
        items = newItems
        notifyDataSetChanged() // один раз — нормально
    }
}
