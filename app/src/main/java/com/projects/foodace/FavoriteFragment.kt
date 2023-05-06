package com.projects.foodace

import android.graphics.Rect
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.DEFAULT_SPAN_COUNT
import androidx.recyclerview.widget.RecyclerView
import com.projects.foodace.databinding.FragmentFavoriteBinding
import com.projects.foodace.model.FavoriteFoodsViewModel
import com.projects.foodace.recyclers.FavoriteFoodsAdapter
import kotlinx.coroutines.launch


class FavoriteFragment : Fragment() {
    private lateinit var binding: FragmentFavoriteBinding
    private val viewModel: FavoriteFoodsViewModel by activityViewModels()

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
        val adapter = FavoriteFoodsAdapter()
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
            viewModel.favoriteFoods.collect {
                adapter.submitList(it)
            }
        }
    }
}