package com.projects.foodace.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.projects.foodace.Event
import java.math.RoundingMode

typealias CartEntry = Pair<Food, Int>

class CartViewModel : ViewModel() {
    val content = MutableLiveData<List<CartEntry>>(listOf())
    private val _foodRemoval = MutableLiveData<Event<CartEntry?>>()
    val foodRemoval = _foodRemoval
    private val _totalCost = MediatorLiveData<Double>()
    val totalCost: LiveData<Double> = _totalCost

    init {
        _totalCost.addSource(content) {
            _totalCost.value = it.sumOf { (food, quantity) -> food.price * quantity }
                .roundToDecimalPlaces(2)
        }
    }

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
            if (newEntry.second == 0) {
                // If quantity becomes 0, item has to be removed
                newContent.removeAt(idx)
                _foodRemoval.value = Event(food to content.value!![idx].second)
            } else {
                newContent.removeAt(idx)
                newContent.add(newEntry)
            }
        }

        content.value = newContent

        Log.v("CART", "Cart is now ${content.value}")
    }

    fun removeItem(food: Food) {
        val newContent = content.value!!.toMutableList()

        val idx = findEntry(food)
        if (idx == -1)
            throw IllegalArgumentException("${food.name} is not in cart and cannot be removed.")
        else {
            newContent.removeAt(idx)
            _foodRemoval.value = Event(food to content.value!![idx].second)
        }

        content.value = newContent

        Log.v("CART", "Cart is now ${content.value}")
    }

    override fun onCleared() {
        super.onCleared()
        foodRemoval.value = null
    }

    private fun findEntry(food: Food) =
        content.value!!.indexOfFirst { (inCartFood) -> food.name == inCartFood.name }
}

fun Double.roundToDecimalPlaces(places: Int) =
    this.toBigDecimal()
        .setScale(2, RoundingMode.FLOOR)
        .toDouble()