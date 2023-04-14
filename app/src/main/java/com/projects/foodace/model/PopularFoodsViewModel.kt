package com.projects.foodace.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.projects.foodace.database.FoodAceRepository

class PopularFoodsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = FoodAceRepository(application)

    val popularFoods = repository.getPopularFoods()
}