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
import com.projects.foodace.databinding.PopularFoodViewHolderBinding
import com.projects.foodace.model.Food

class PopularFoodsAdapter(private val detailsActivityLauncher: ActivityResultLauncher<Food>)
    : ListAdapter<Food, PopularFoodsAdapter.ViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.popular_food_view_holder, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val food = getItem(position)
        holder.binding.foodName.text = food.name
        holder.binding.foodPrice.text =
            holder.itemView.context.getString(R.string.price_string, food.price)

        val color = ContextCompat.getColor(holder.itemView.context, food.category.bgoundColor)
        holder.binding.foodCard.setCardBackgroundColor(color)

        Glide.with(holder.itemView)
            .load(food.img)
            .transform(RoundedCorners(16))
            .into(holder.binding.foodImg)

        holder.itemView.setOnClickListener {
            detailsActivityLauncher.launch(getItem(position))
        }
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Food>() {
            override fun areItemsTheSame(oldItem: Food, newItem: Food): Boolean {
                return oldItem.name == newItem.name
            }
            override fun areContentsTheSame(oldItem: Food, newItem: Food): Boolean {
                return oldItem == newItem
            }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = PopularFoodViewHolderBinding.bind(itemView)
    }
}