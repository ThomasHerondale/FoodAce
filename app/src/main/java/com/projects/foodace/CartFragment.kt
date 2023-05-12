package com.projects.foodace

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.activity.result.contract.ActivityResultContract
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import com.google.android.material.snackbar.Snackbar
import com.projects.foodace.databinding.FragmentCartBinding
import com.projects.foodace.model.CartEntry
import com.projects.foodace.model.CartViewModel
import com.projects.foodace.recyclers.CartEntriesAdapter

class CartFragment : NavHostFragment() {
    private lateinit var binding: FragmentCartBinding
    private val viewModel: CartViewModel by lazy { applicationViewModels() }

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
            taxesCost.text = getString(R.string.price_string, taxesAmt)
            delCost.text = getString(R.string.price_string, deliveryFeesAmt)
            viewModel.totalCost.observe(viewLifecycleOwner) {
                foodCost.text = getString(R.string.price_string, it)

                val totalAmt = it + taxesAmt + deliveryFeesAmt
                total.text = getString(R.string.total, totalAmt)
            }
            orderBttn.isEnabled = viewModel.content.value!!.isNotEmpty()
        }
        return binding.root
    }

    private fun initContentList() {
        val adapter = CartEntriesAdapter(
            viewModel::addOneOf,
            viewModel::removeOneOf,
            viewModel::removeItem,
            binding
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

    private fun showItemRemovedSnackbar(removalInfo: Pair<CartEntry?, Int>) {
        val (item, idx) = removalInfo
        Snackbar.make(
            requireView(),
            getString(R.string.removed_from_cart_snack_text, item?.food?.name),
            Snackbar.LENGTH_SHORT
        ).apply {
            setAnchorView(R.id.navBar)
            setActionTextColor(resources.getColor(R.color.bright_orange))
            setBackgroundTint(resources.getColor(R.color.dark_blue))

            setAction("Undo") {
                viewModel.addItem(item!!, idx)
            }
        }.show()

    }
}