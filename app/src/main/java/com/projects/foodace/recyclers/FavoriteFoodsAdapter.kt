package com.projects.foodace.recyclers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.projects.foodace.R
import com.projects.foodace.databinding.FavoriteFoodViewHolderBinding
import com.projects.foodace.model.Food

class FavoriteFoodsAdapter : ListAdapter<Food, FavoriteFoodsAdapter.ViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.favorite_food_view_holder, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val food = getItem(position)
        holder.binding.apply {
            foodName.text = food.name

            Glide.with(holder.itemView)
                .load(food.img)
                .into(foodImg)
        }
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Food>() {
            override fun areItemsTheSame(oldItem: Food, newItem: Food): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: Food, newItem: Food): Boolean {
                return oldItem.name == newItem.name
            }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = FavoriteFoodViewHolderBinding.bind(itemView)
    }
}