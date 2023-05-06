package com.projects.foodace

import android.content.Intent
import android.opengl.GLU
import android.os.Bundle
import android.util.Log
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.projects.foodace.database.FoodAceRepository
import com.projects.foodace.databinding.ActivityFoodDetailsBinding
import com.projects.foodace.model.Food
import com.projects.foodace.model.FoodDetailsViewModel
import com.projects.foodace.model.FoodDetailsViewModelFactory

class FoodDetailsActivity : AppCompatActivity() {
    private val viewModel: FoodDetailsViewModel by viewModels { FoodDetailsViewModelFactory(
        intent.extras?.get("food") as Food, application
    ) }
    private lateinit var binding: ActivityFoodDetailsBinding
    private val repository by lazy { FoodAceRepository(application) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodDetailsBinding.inflate(layoutInflater)

        viewModel.food.observe(this) {
            binding.foodTitleDet.text = it.name
            binding.foodPriceDet.text = getString(R.string.price_string, it.price)
            binding.foodDescr.text = it.description
            Glide.with(binding.root)
                .load(it.img)
                .transform(RoundedCorners(24))
                .into(binding.foodImgDet)
            binding.favoriteBttn.setImageDrawable(
                AppCompatResources.getDrawable(this, favoriteButtonImg())
            )
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

        binding.favoriteBttn.setOnClickListener {
            repository.setFavorite(viewModel.food.value!!)
            Log.d("FAV", "${viewModel.food.value}")
            binding.favoriteBttn.setImageDrawable(
                AppCompatResources.getDrawable(this, favoriteButtonImg())
            )
        }

        setContentView(binding.root)

        onBackPressedDispatcher.addCallback(this) {
            val intent = Intent().apply {
                putExtra("food", viewModel.food.value)
                putExtra("quantity", 0)
            }
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    private fun favoriteButtonImg() =
        if (viewModel.food.value!!.isFavorite)
            R.drawable.star_icon_filled
        else
            R.drawable.star_icon_outlined
}