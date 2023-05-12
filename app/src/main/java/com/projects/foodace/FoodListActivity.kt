package com.projects.foodace

import android.app.SearchManager
import android.content.Intent.ACTION_SEARCH
import android.os.Bundle
import android.widget.SearchView.OnQueryTextListener
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import com.projects.foodace.databinding.ActivityFoodListBinding
import com.projects.foodace.model.CartViewModel
import com.projects.foodace.model.Category
import com.projects.foodace.model.FoodListViewModel
import com.projects.foodace.recyclers.FoodEntryAdapter

const val CATEGORY_KEY = "category"

class FoodListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFoodListBinding
    private val foodListViewModel: FoodListViewModel by viewModels()
    private val cartViewModel: CartViewModel by lazy { applicationViewModels() }

    private val detailsActivityLauncher = registerForActivityResult(AddToCartContract()) {
        if (it.quantity != 0)
            cartViewModel.addItem(it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        when (intent.action) {
            ACTION_SEARCH ->
                intent.getStringExtra(SearchManager.QUERY)?.also { foodListViewModel.getFoods(it) }
            FILTER ->
                intent.extras?.get(CATEGORY_KEY)?.also {
                    foodListViewModel.getFoods(category = it as Category)
                }
            else -> TODO()
        }

        binding.searchBar.apply {
            setOnQueryTextListener(object : OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (query != null) {
                        foodListViewModel.getFoods(query)
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })
            clearFocus()
        }

        binding.backButton.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
        initFoodList()
    }

    private fun initFoodList() {
        val adapter = FoodEntryAdapter(detailsActivityLauncher)
        binding.foodList.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(context, VERTICAL, false)
        }
        foodListViewModel.foods.observe(this) { adapter.submitList(it) }
    }
}