package com.projects.foodace.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.projects.foodace.database.FoodAceRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class FoodListViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = FoodAceRepository(application)
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    private lateinit var foodsFlow: Flow<List<Food>>
    private var _foods = MutableLiveData<List<Food>>()
    val foods: LiveData<List<Food>> = _foods


    fun getFoods(query: String) {
        foodsFlow = repository.searchFoods(query)
        coroutineScope.launch { foodsFlow.collect { _foods.postValue(it)} }
    }
}
