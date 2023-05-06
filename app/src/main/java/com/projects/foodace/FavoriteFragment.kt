package com.projects.foodace

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.GridLayoutManager
import com.projects.foodace.databinding.FragmentFavoriteBinding
import com.projects.foodace.model.CartViewModel
import com.projects.foodace.model.FavoriteFoodsViewModel
import com.projects.foodace.recyclers.FavoriteFoodsAdapter
import kotlinx.coroutines.launch


class FavoriteFragment : Fragment() {
    private lateinit var binding: FragmentFavoriteBinding
    private val favoriteFoodsViewModel: FavoriteFoodsViewModel by activityViewModels()
    private val cartViewModel: CartViewModel by activityViewModels()

    private val detailsActivityLauncher = registerForActivityResult(AddToCartContract()) {
        if (it.quantity != 0)
            cartViewModel.addItem(it)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentFavoriteBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initFavoritesList()
    }

    private fun initFavoritesList() {
        val adapter = FavoriteFoodsAdapter(detailsActivityLauncher)
        binding.favoritesList.apply {
            this.adapter = adapter
            layoutManager = GridLayoutManager(
                context,
                3,
                GridLayoutManager.VERTICAL,
                false
            )
        }
        lifecycle.coroutineScope.launch {
            favoriteFoodsViewModel.favoriteFoods.collect {
                adapter.submitList(it)
            }
        }
    }
}