package com.projects.foodace.recyclers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.projects.foodace.R
import com.projects.foodace.databinding.CartEntryViewHolderBinding
import com.projects.foodace.model.CartEntry
import com.projects.foodace.model.Food

class CartEntriesAdapter(
    private val onPlusButtonClicked: (Food) -> Unit,
    private val onMinusButtonClicked: (Food) -> Unit,
    private val onDeleteButtonClicked: (Food) -> Unit
) : ListAdapter<CartEntry, CartEntriesAdapter.ViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.cart_entry_view_holder, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (food, quantity) = getItem(position)

        holder.binding.apply {
            foodNameCart.text = food.name
            foodDescrCart.text = food.description
            quantityCart.text = "$quantity"

            Glide.with(holder.itemView)
                .load(food.img)
                .transform(RoundedCorners(16))
                .into(foodImgCart)

            plusBttnCart.setOnClickListener { onPlusButtonClicked(food) }
            subBttnCart.setOnClickListener { onMinusButtonClicked(food) }
            delBttnCart.setOnClickListener { onDeleteButtonClicked(food) }
        }
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<CartEntry>() {
            override fun areItemsTheSame(oldItem: CartEntry, newItem: CartEntry): Boolean {
                return oldItem.first == newItem.first
            }

            override fun areContentsTheSame(oldItem: CartEntry, newItem: CartEntry): Boolean {
               return oldItem.second == newItem.second
            }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding =  CartEntryViewHolderBinding.bind(itemView)
    }
}