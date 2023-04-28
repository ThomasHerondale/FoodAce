package com.projects.foodace.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

typealias CartEntry = Pair<Food, Int>

class CartViewModel : ViewModel() {
    val content = MutableLiveData<List<CartEntry>>(listOf())

    private fun addItem(food: Food, quantity: Int) {
        Log.i("CART", "Adding to cart $quantity of ${food.name}")

        val newContent = content.value!!.toMutableList()

        val idx = findEntry(food)
        if (idx == -1)
            newContent.add(food to quantity)
        else {
            val newEntry = food to (newContent[idx].second + quantity)
            newContent.removeAt(idx)
            newContent.add(newEntry)
        }

        content.value = newContent

        Log.v("CART", "Cart is now ${content.value}")
    }

    fun addItem(entry: CartEntry) { addItem(entry.first, entry.second) }

    fun addOneOf(food: Food) { addItem(food, 1)}

    fun removeOneOf(food: Food) {
        val newContent = content.value!!.toMutableList()

        val idx = findEntry(food)
        if (idx == -1)
            throw IllegalArgumentException("${food.name} is not in cart and cannot be removed.")
        else {
            val newEntry = food to (newContent[idx].second - 1)
            if (newEntry.second == 0)
                // If quantity becomes 0, item has to be removed
                newContent.removeAt(idx)
            else {
                newContent.removeAt(idx)
                newContent.add(newEntry)
            }
        }

        content.value = newContent

        Log.v("CART", "Cart is now ${content.value}")
    }

    private fun findEntry(food: Food) =
        content.value!!.indexOfFirst { (inCartFood) -> food.name == inCartFood.name }
}