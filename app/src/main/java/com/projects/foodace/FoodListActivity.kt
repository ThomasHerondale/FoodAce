package com.projects.foodace

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.projects.foodace.databinding.ActivityFoodListBinding
import com.projects.foodace.model.FoodListViewModel

class FoodListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFoodListBinding
    private val viewModel: FoodListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.action == Intent.ACTION_SEARCH)
            intent.getStringExtra(SearchManager.QUERY)?.also {
               // viewModel.getFoods(it)
                binding.textView5.text = it
            }
    }
}