package com.projects.foodace

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.projects.foodace.model.CartEntry
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

        binding.apply {
            // TODO: dinamiche in base a qualcosa?
            val taxesAmt = 2.9
            val deliveryFeesAmt = 3.5
            taxes.text = getString(R.string.taxes_tot, taxesAmt)
            delFees.text = getString(R.string.del_fees_tot, deliveryFeesAmt)
            viewModel.totalCost.observe(viewLifecycleOwner) {
                foodTot.text = getString(R.string.food_tot, it)

                val totalAmt = it + taxesAmt + deliveryFeesAmt
                total.text = getString(R.string.total, totalAmt)
            }
        }
        return binding.root
    }

    private fun initContentList() {
        val adapter = CartEntriesAdapter(
            viewModel::addOneOf,
            viewModel::removeOneOf,
            viewModel::removeItem
        )

        binding.contentList.adapter = adapter
        binding.contentList.layoutManager =
            LinearLayoutManager(context, VERTICAL, false)

        viewModel.content.observe(viewLifecycleOwner) {
            Log.v("CART", "Updating cart recyclerview")
            adapter.submitList(it)
        }

        viewModel.foodRemoval.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let {
                showItemRemovedSnackbar(it)
            }
        }

        viewModel.totalCost.observe(viewLifecycleOwner) { Log.d("CART", "Price: $it") }
    }

    private fun showItemRemovedSnackbar(item: CartEntry) {
        Snackbar.make(
            requireView(),
            getString(R.string.removed_from_cart_snack_text, item.food.name),
            Snackbar.LENGTH_SHORT
        ).apply {
            setAnchorView(R.id.navBar)
            setActionTextColor(resources.getColor(R.color.bright_orange))
            setBackgroundTint(resources.getColor(R.color.dark_blue))

            setAction("Undo") {
                viewModel.addItem(item)
            }
        }.show()

    }
}

class AddToCartContract : ActivityResultContract<Food, CartEntry>() {
    override fun createIntent(context: Context, input: Food) =
        Intent(context, FoodDetailsActivity::class.java).apply {
            putExtra("food", input)
        }

    override fun parseResult(resultCode: Int, intent: Intent?): CartEntry {
        return if (resultCode == RESULT_OK) {
            val quantity = intent?.extras!!.get("quantity") as Int
            val food = intent.extras!!.get("food") as Food
            CartEntry(food, quantity)
        } else
            throw IllegalStateException("ShowDetailsActivity did not provide quantity.")
    }

}