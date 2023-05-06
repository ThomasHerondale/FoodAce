package com.projects.foodace

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.projects.foodace.model.CartEntry
import com.projects.foodace.model.Food

class AddToCartContract : ActivityResultContract<Food, CartEntry>() {
    override fun createIntent(context: Context, input: Food) =
        Intent(context, FoodDetailsActivity::class.java).apply {
            putExtra("food", input)
        }

    override fun parseResult(resultCode: Int, intent: Intent?): CartEntry {
        return if (resultCode == Activity.RESULT_OK) {
            val quantity = intent?.extras!!.get("quantity") as Int
            val food = intent.extras!!.get("food") as Food
            CartEntry(food, quantity)
        } else
            throw IllegalStateException("ShowDetailsActivity did not provide quantity.")
    }
}