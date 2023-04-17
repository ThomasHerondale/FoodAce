package com.projects.foodace

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContract
import androidx.navigation.fragment.NavHostFragment
import com.projects.foodace.model.Food

class CartFragment : NavHostFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }
}

class AddToCartContract : ActivityResultContract<Food, Pair<Food, Int>>() {
    override fun createIntent(context: Context, input: Food) =
        Intent(context, FoodDetailsActivity::class.java).apply {
            putExtra("food", input)
        }

    override fun parseResult(resultCode: Int, intent: Intent?): Pair<Food, Int> {
        return if (resultCode == RESULT_OK)
            intent!!.extras!!.get("food") as Food to intent.extras!!.get("quantity") as Int
        else
            throw IllegalStateException("ShowDetailsActivity did not provide quantity.")
    }

}