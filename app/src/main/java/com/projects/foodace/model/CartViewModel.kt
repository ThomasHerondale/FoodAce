package com.projects.foodace.model

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.projects.foodace.Event
import com.projects.foodace.LoggedApplication
import com.projects.foodace.database.FoodAceRepository
import com.projects.foodace.model.Food.Companion.popularFoodsList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.math.RoundingMode
import kotlin.coroutines.coroutineContext

class CartViewModel(application: Application) : AndroidViewModel(application) {
    val content = MutableLiveData<List<CartEntry>>(listOf())
    private val _foodRemoval = MutableLiveData<Event<CartEntry?>>()
    val foodRemoval = _foodRemoval
    private val _totalCost = MediatorLiveData<Double>()
    val totalCost: LiveData<Double> = _totalCost

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    init {
        _totalCost.addSource(content) {
            _totalCost.value = it.sumOf { (food, quantity) -> food.price * quantity }
                .roundToDecimalPlaces(2)
        }
        val username = (application as LoggedApplication).username!!

        coroutineScope.launch {
            FoodAceRepository(application)
                .restoreCart(username).collect {content.postValue(it)}
        }
    }

    private fun addItem(food: Food, quantity: Int) {
        Log.i("CART", "Adding to cart $quantity of ${food.name}")

        val newContent = content.value!!.toMutableList()

        val idx = findEntry(food)
        if (idx == -1)
            newContent.add(CartEntry(food, quantity))
        else {
            val newEntry = CartEntry(food, newContent[idx].quantity + quantity)
            newContent.removeAt(idx)
            newContent.add(idx, newEntry)
        }

        content.value = newContent

        Log.v("CART", "Cart is now ${content.value}")
    }

    fun addItem(entry: CartEntry) { addItem(entry.food, entry.quantity) }

    fun addOneOf(food: Food) { addItem(food, 1)}

    fun removeOneOf(food: Food) {
        val newContent = content.value!!.toMutableList()

        val idx = findEntry(food)
        if (idx == -1)
            throw IllegalArgumentException("${food.name} is not in cart and cannot be removed.")
        else {
            val newEntry = CartEntry(food, newContent[idx].quantity - 1)
            if (newEntry.quantity == 0) {
                // If quantity becomes 0, item has to be removed
                newContent.removeAt(idx)
                _foodRemoval.value = Event(CartEntry(food, content.value!![idx].quantity))
            } else {
                newContent.removeAt(idx)
                newContent.add(idx, newEntry)
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
            _foodRemoval.value = Event(CartEntry(food, content.value!![idx].quantity))
        }

        content.value = newContent

        Log.v("CART", "Cart is now ${content.value}")
    }

    override fun onCleared() {
        super.onCleared()
        foodRemoval.value = null
        coroutineScope.cancel()
    }

    private fun findEntry(food: Food) =
        content.value!!.indexOfFirst { (inCartFood) -> food.name == inCartFood.name }
}

fun Double.roundToDecimalPlaces(places: Int) =
    this.toBigDecimal()
        .setScale(places, RoundingMode.FLOOR)
        .toDouble()