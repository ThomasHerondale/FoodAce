package com.projects.foodace.model

data class CartEntry(val food: Food, val quantity: Int) {
    override fun toString(): String {
        return "(${food.name}, $quantity)"
    }
}
