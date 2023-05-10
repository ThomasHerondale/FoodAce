package com.projects.foodace.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.projects.foodace.database.FoodAceRepository
import kotlinx.coroutines.flow.Flow

class FoodListViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = FoodAceRepository(application)

    lateinit var foods: Flow<List<Food>>


    fun getFoods(query: String) {
        foods = repository.searchFoods(query)
    }
}
