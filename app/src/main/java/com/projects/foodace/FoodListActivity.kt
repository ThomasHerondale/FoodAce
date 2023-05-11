package com.projects.foodace

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.widget.SearchView.OnQueryTextListener
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
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

        binding.searchBar.apply {
            setOnQueryTextListener(object : OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (query != null) {
                        println("change")
                        viewModel.getFoods(query)
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })
        }

        binding.backButton.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
        initFoodList()
    }

    private fun initFoodList() {
        val adapter = FoodEntryAdapter()
        binding.foodList.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(context, VERTICAL, false)
        }
        viewModel.foods.observe(this) { adapter.submitList(it) }
    }
}