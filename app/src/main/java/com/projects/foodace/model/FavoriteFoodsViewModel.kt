package com.projects.foodace.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.projects.foodace.database.FoodAceRepository

class FavoriteFoodsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = FoodAceRepository(application)

    val favoriteFoods = repository.getFavoriteFoods()
}