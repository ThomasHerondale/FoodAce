package com.projects.foodace

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import com.projects.foodace.databinding.ActivityFoodListBinding
import com.projects.foodace.model.FoodListViewModel
import com.projects.foodace.recyclers.FoodEntryAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class FoodListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFoodListBinding
    private val viewModel: FoodListViewModel by viewModels()
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.action == Intent.ACTION_SEARCH)
            intent.getStringExtra(SearchManager.QUERY)?.also { viewModel.getFoods(it) }
        else
            TODO()

        initFoodList()
    }

    private fun initFoodList() {
        val adapter = FoodEntryAdapter()
        binding.foodList.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(context, VERTICAL, false)
        }
        coroutineScope.launch {
            viewModel.foods.collect {
                adapter.submitList(it)
            }
        }
    }
}