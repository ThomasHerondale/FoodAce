package com.projects.foodace

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.projects.foodace.databinding.ActivityFoodDetailsBinding
import com.projects.foodace.model.Food
import com.projects.foodace.model.FoodDetailsViewModel
import com.projects.foodace.model.FoodDetailsViewModelFactory

class FoodDetailsActivity : AppCompatActivity() {
    private val viewModel: FoodDetailsViewModel by viewModels { FoodDetailsViewModelFactory(
        intent.extras?.get("food") as Food, application
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

        binding.addToCartButton.setOnClickListener {
            val intent = Intent().apply {
                putExtra("food", viewModel.food.value)
                putExtra("quantity", viewModel.quantity.value)
            }
            setResult(RESULT_OK, intent)
            finish()
        }

        setContentView(binding.root)
    }
}