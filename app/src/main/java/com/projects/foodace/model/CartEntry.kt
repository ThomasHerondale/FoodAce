package com.projects.foodace.model

import com.projects.foodace.database.UserCartEntry

data class CartEntry(val food: Food, val quantity: Int) {

    constructor(entry: UserCartEntry) : this(entry.food, entry.quantity)

    override fun toString(): String {
        return "(${food.name}, $quantity)"
    }
}
