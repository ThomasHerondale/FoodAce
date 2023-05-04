package com.projects.foodace.recyclers

import android.os.Bundle
import android.util.Log
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
import com.projects.foodace.databinding.FragmentCartBinding
import com.projects.foodace.model.CartEntry
import com.projects.foodace.model.Food
import com.projects.foodace.model.roundToDecimalPlaces

class CartEntriesAdapter(
    private val onPlusButtonClicked: (Food) -> Unit,
    private val onMinusButtonClicked: (Food) -> Unit,
    private val onDeleteButtonClicked: (Food) -> Unit,
    private val binding: FragmentCartBinding
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
            val totalPrice = (quantity * food.price).roundToDecimalPlaces(2)
            totalPriceCart.text = root.resources.getString(R.string.price_string, totalPrice)

            Glide.with(holder.itemView)
                .load(food.img)
                .transform(RoundedCorners(16))
                .into(foodImgCart)

            plusBttnCart.setOnClickListener { onPlusButtonClicked(food) }
            subBttnCart.setOnClickListener { onMinusButtonClicked(food) }
            delBttnCart.setOnClickListener { onDeleteButtonClicked(food) }
        }
    }

    override fun onCurrentListChanged(
        previousList: MutableList<CartEntry>,
        currentList: MutableList<CartEntry>
    ) {
        if (currentList.isEmpty()) {
            binding.emptyCartText.visibility = View.VISIBLE
            binding.contentList.visibility = View.GONE
        } else {
            binding.emptyCartText.visibility = View.GONE
            binding.contentList.visibility = View.VISIBLE
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty())
            onBindViewHolder(holder, position)
        else {
            val payload = payloads[0] as Bundle
            val quantity = payload.get("quantity") as Int
            val totalPrice = payload.get("totalPrice") as Double

            holder.binding.apply {
                quantityCart.text = "$quantity"
                totalPriceCart.text =
                    root.resources.getString(R.string.price_string, totalPrice)
            }

        }
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<CartEntry>() {
            override fun areItemsTheSame(oldItem: CartEntry, newItem: CartEntry): Boolean {
                val result = oldItem.food.name == newItem.food.name
                Log.v("CARTDIFF",
                    "Checking ${oldItem.food.name} vs " +
                            "${newItem.food.name} - Result: $result")
                return result
            }

            override fun areContentsTheSame(oldItem: CartEntry, newItem: CartEntry): Boolean {
                val result = oldItem.quantity == newItem.quantity
                Log.v("CARTDIFF",
                    "Checking ${oldItem.food.name}, ${oldItem.quantity} vs " +
                            "${newItem.food.name}, ${newItem.quantity} - Result: $result")
                return result
            }

            override fun getChangePayload(oldItem: CartEntry, newItem: CartEntry): Any {
                return Bundle().apply {
                    putInt("quantity", newItem.quantity)
                    putDouble("totalPrice", newItem.quantity * newItem.food.price)
                }
            }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding =  CartEntryViewHolderBinding.bind(itemView)
    }
}