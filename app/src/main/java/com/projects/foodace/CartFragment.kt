package com.projects.foodace

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContract
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import com.google.android.material.snackbar.Snackbar
import com.projects.foodace.databinding.FragmentCartBinding
import com.projects.foodace.model.CartViewModel
import com.projects.foodace.model.Food
import com.projects.foodace.recyclers.CartEntriesAdapter

class CartFragment : NavHostFragment() {
    private lateinit var binding: FragmentCartBinding
    private val viewModel: CartViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentCartBinding.inflate(layoutInflater)
        initContentList()
        return binding.root
    }

    private fun initContentList() {
        val adapter = CartEntriesAdapter(viewModel::addOneOf, viewModel::removeOneOf)

        binding.contentList.adapter = adapter
        binding.contentList.layoutManager =
            LinearLayoutManager(context, VERTICAL, false)

        viewModel.content.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.foodRemoval.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let {
                showItemRemovedSnackbar(it)
            }
        }
    }

    private fun showItemRemovedSnackbar(food: Food) {
        val snackbar = Snackbar.make(
            requireView(),
            getString(R.string.removed_from_cart_snack_text, food.name),
            Snackbar.LENGTH_SHORT
        )
        snackbar.setAction("Undo") {
            viewModel.addOneOf(food)
        }
        snackbar.show()
    }
}

class AddToCartContract : ActivityResultContract<Food, Pair<Food, Int>>() {
    override fun createIntent(context: Context, input: Food) =
        Intent(context, FoodDetailsActivity::class.java).apply {
            putExtra("food", input)
        }

    override fun parseResult(resultCode: Int, intent: Intent?): Pair<Food, Int> {
        return if (resultCode == RESULT_OK)
            intent!!.extras!!.get("food") as Food to intent.extras!!.get("quantity") as Int
        else
            throw IllegalStateException("ShowDetailsActivity did not provide quantity.")
    }

}