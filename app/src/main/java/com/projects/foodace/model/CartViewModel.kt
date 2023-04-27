package com.projects.foodace.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

typealias CartEntry = Pair<Food, Int>

class CartViewModel : ViewModel() {
    private val content = MutableLiveData<List<CartEntry>>(listOf())

    private fun addItems(food: Food, quantity: Int) {
        Log.i("CART", "Adding to cart $quantity of ${food.name}")

        val newContent = content.value!!.toMutableList()

        val idx = content.value!!.indexOfFirst { (inCartFood) -> food.name == inCartFood.name }
        if (idx == -1)
            newContent.add(food to quantity)
        else {
            val newEntry = food to (newContent[idx].second + quantity)
            newContent.add(newEntry)
            newContent.removeAt(idx)
        }

        content.value = newContent

        Log.v("CART", "Cart is now ${content.value}")
    }

    fun addItems(entry: CartEntry) { addItems(entry.first, entry.second) }
}