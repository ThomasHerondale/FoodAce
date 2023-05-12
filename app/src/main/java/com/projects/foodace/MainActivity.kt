package com.projects.foodace

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.projects.foodace.databinding.ActivityMainBinding
import com.projects.foodace.model.CartViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    private lateinit var binding: ActivityMainBinding
    private val navController by lazy {
        (binding.pageFragmentView.getFragment() as NavHostFragment).navController
    }
    private val navigationView by lazy { binding.navBar }
    private val cartViewModel by lazy { (application as FoodAceApplication).cartViewModel }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navigationView.setupWithNavController(navController)

        setupAccountFragment()
    }

    private fun setupAccountFragment() {
        val fragment = AccountFragment.newInstance(
            intent.getStringExtra("username")!!
        )
        supportFragmentManager
            .beginTransaction()
            .add(R.id.accountFragmentView, fragment)
            .commit()
    }
}