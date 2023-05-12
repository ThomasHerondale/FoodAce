package com.projects.foodace.recyclers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.projects.foodace.R
import com.projects.foodace.databinding.FoodListViewHolderBinding
import com.projects.foodace.model.Food

class FoodEntryAdapter(private val detailsActivityLauncher: ActivityResultLauncher<Food>)
    : ListAdapter<Food, FoodEntryAdapter.ViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.food_list_view_holder, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val food = getItem(position)

        holder.binding.apply {
            if (!food.isFavorite)
                favIcon.visibility = View.GONE
            foodNameEntry.text = food.name
            foodDescrEntry.text = food.description

            val color = ContextCompat.getColor(holder.itemView.context, food.category.bgoundColor)
            foodCardEntry.setCardBackgroundColor(color)
            foodCardEntry.setOnClickListener { detailsActivityLauncher.launch(food) }

            Glide.with(holder.itemView)
                .load(food.img)
                .transform(RoundedCorners(12))
                .into(foodImgEntry)

            // Workaround per avere il bordo solo se il cibo è nei preferiti
            holder.binding.foodCardEntry.isSelected = food.isFavorite
        }
    }

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<Food>() {
            override fun areItemsTheSame(oldItem: Food, newItem: Food): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Food, newItem: Food): Boolean {
                return oldItem == newItem
            }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = FoodListViewHolderBinding.bind(itemView)
    }
}