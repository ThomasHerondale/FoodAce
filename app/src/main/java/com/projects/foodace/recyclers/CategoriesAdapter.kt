package com.projects.foodace.recyclers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.bumptech.glide.Glide
import com.projects.foodace.R
import com.projects.foodace.databinding.CategoryViewHolderBinding
import com.projects.foodace.model.Category

class CategoriesAdapter(
    private val activityLauncher: (Category) -> Unit
) : Adapter<CategoriesAdapter.ViewHolder>() {
    private val categories = Category.values()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.category_view_holder, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount() = categories.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = categories[position]
        holder.binding.categoryName.text = category.name

        val color = ContextCompat.getColor(holder.itemView.context, category.bgoundColor)
        holder.binding.categoryCard.setCardBackgroundColor(color)

        Glide.with(holder.itemView)
            .load(category.img)
            .into(holder.binding.categoryImg)

        holder.binding.categoryCard.setOnClickListener { activityLauncher(category) }
    }

    override fun onViewRecycled(holder: ViewHolder) {
        Glide.with(holder.itemView)
            .clear(holder.binding.categoryImg)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = CategoryViewHolderBinding.bind(itemView)
    }

}

