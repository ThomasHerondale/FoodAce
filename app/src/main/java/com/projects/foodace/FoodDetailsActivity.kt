package com.projects.foodace

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.projects.foodace.databinding.ActivityFoodDetailsBinding
import com.projects.foodace.model.Category
import com.projects.foodace.model.Food
import com.projects.foodace.model.FoodDetailsViewModel
import com.projects.foodace.model.FoodDetailsViewModelFactory

class FoodDetailsActivity : AppCompatActivity() {
    private val viewModel: FoodDetailsViewModel by viewModels { FoodDetailsViewModelFactory(
        Food("Margherita", Category.PIZZA, "Pomodoro, mozzarella, origano",
            7.5, R.drawable.pizza, true), application
    ) }
    private lateinit var binding: ActivityFoodDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodDetailsBinding.inflate(layoutInflater)

        viewModel.food.observe(this) {
            binding.foodTitleDet.text = it.name
            binding.foodPriceDet.text = getString(R.string.price_string, it.price)
            binding.foodDescr.text = it.description
            Glide.with(binding.root)
                .load(it.img)
                .into(binding.foodImgDet)
        }

        viewModel.quantity.observe(this) {
            binding.quantity.text = "${viewModel.quantity.value}"
            // Disable subButton when counter is 0
            binding.subButton.isEnabled = viewModel.quantity.value!! > 0
            binding.addToCartButton.isEnabled = viewModel.quantity.value!! > 0
        }

        binding.addButton.setOnClickListener {
            viewModel.quantity.value = viewModel.quantity.value?.plus(1)
        }

        binding.subButton.setOnClickListener {
            viewModel.quantity.value = viewModel.quantity.value?.minus(1)
        }

        setContentView(binding.root)
    }
}