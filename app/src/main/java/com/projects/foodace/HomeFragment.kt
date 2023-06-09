package com.projects.foodace

import android.app.SearchManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.projects.foodace.databinding.FragmentHomeBinding
import com.projects.foodace.model.CartViewModel
import com.projects.foodace.model.PopularFoodsViewModel
import com.projects.foodace.recyclers.CategoriesAdapter
import com.projects.foodace.recyclers.PopularFoodsAdapter
import kotlinx.coroutines.launch

class HomeFragment : NavHostFragment() {
    private lateinit var binding: FragmentHomeBinding
    private val popularFoodsViewModel: PopularFoodsViewModel by activityViewModels()
    private val cartViewModel: CartViewModel by lazy { applicationViewModels() }

    private val detailsActivityLauncher = registerForActivityResult(AddToCartContract()) {
        if (it.quantity != 0) // Quantity can be 0 if user exits using back button
            cartViewModel.addItem(it)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initCategoriesList()
        initPopularFoodsList()

        val searchManager = requireActivity()
            .getSystemService(Context.SEARCH_SERVICE) as SearchManager

        binding.mainSearchBar.
            setSearchableInfo(searchManager.getSearchableInfo(
                ComponentName(requireContext(), FoodListActivity::class.java))
            )
    }

    override fun onResume() {
        super.onResume()
        // Search bar gets not selected on resume
        binding.mainSearchBar.clearFocus()
    }

    private fun initCategoriesList() {
        Log.v("Init", "Initializing categories RecyclerView")
        binding.categoriesList.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = CategoriesAdapter {
                val intent = Intent(context, FoodListActivity::class.java).apply {
                    action = FILTER
                    putExtra("category", it)
                }
                startActivity(intent)
            }
        }
    }

    private fun initPopularFoodsList() {
        Log.v("Init", "Initializing popular foods RecyclerView")
        binding.popularsList.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            val foodsAdapter = PopularFoodsAdapter(detailsActivityLauncher)
            adapter = foodsAdapter
            lifecycle.coroutineScope.launch {
                popularFoodsViewModel.popularFoods.collect {
                    foodsAdapter.submitList(it)
                }
            }
        }
    }
}