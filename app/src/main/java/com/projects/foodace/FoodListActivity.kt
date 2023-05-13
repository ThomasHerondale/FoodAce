package com.projects.foodace

import android.app.SearchManager
import android.content.Intent.ACTION_SEARCH
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView.OnQueryTextListener
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import com.google.android.material.chip.Chip
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

    // ChipGroup handling
    private val selections = Array( 5) { false }

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
                intent.getStringExtra(SearchManager.QUERY)?.also {
                    foodListViewModel.getFoods(it)
                    binding.searchBar.setQuery(it, false)
                }
            FILTER ->
                intent.extras?.get(CATEGORY_KEY)?.also {
                    foodListViewModel.getFoods(categories = listOf(it as Category))
                    checkCategory(it)
                }
            else -> TODO()
        }

        binding.searchBar.apply {
            setOnQueryTextListener(object : OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (query != null) {
                        foodListViewModel.getFoods(query, selectedCategories())
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    // Handle the x button in the search bar
                    if (newText?.isEmpty() == true)
                        foodListViewModel.getFoods(categories = selectedCategories())
                    return true
                }
            })
            // Don't show keyboard on start
            clearFocus()
        }

        binding.backButton.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
        initFoodList()
        initChipGroup()
    }

    private fun initFoodList() {
        val adapter = FoodEntryAdapter(detailsActivityLauncher)
        binding.foodList.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(context, VERTICAL, false)
        }
        foodListViewModel.foods.observe(this) { adapter.submitList(it) }
    }

    private fun initChipGroup() {
        binding.categoriesChips.children.forEach { view: View ->
            val chip = view as Chip
            chip.setOnCheckedChangeListener { _, _ ->
                binding.categoriesChips.apply {
                    val checkedIdxs = checkedChipIds.map { getChipIndex(it) }
                    for (idx in selections.indices)
                        selections[idx] = idx in checkedIdxs
                    Log.v("FODD_LIST", "Filtering for ${selections.contentToString()}")
                }
                val query = binding.searchBar.query.toString()
                foodListViewModel.getFoods(query, selectedCategories())
            }
        }
    }

    private fun getChipIndex(id: Int) = when (id) {
        R.id.burgerChip -> 0
        R.id.pizzaChip -> 1
        R.id.hotdogChip -> 2
        R.id.drinkChip -> 3
        R.id.dessertChip -> 4
        else -> throw IllegalStateException("Unknown chip id.")
    }

    private fun selectedCategories(): List<Category>? {
        val list = mutableListOf<Category>()
        Category.values().forEachIndexed { index, category ->
            if (selections[index])
                list.add(category)
        }
        return list.ifEmpty { null }
    }

    private fun checkCategory(category: Category) {
        val idx = Category.values().indexOf(category)
        (binding.categoriesChips[idx] as Chip).isChecked = true
    }
}