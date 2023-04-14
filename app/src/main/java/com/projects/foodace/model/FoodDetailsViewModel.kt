package com.projects.foodace.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class FoodDetailsViewModel(food: Food, application: Application)
    : AndroidViewModel(application) {

    private val _food = MutableLiveData(food)
    val food: LiveData<Food> = _food
    val quantity = MutableLiveData(0)
}

class FoodDetailsViewModelFactory(
    private val food: Food,
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FoodDetailsViewModel::class.java))
            return FoodDetailsViewModel(food, application) as T
        else
            throw IllegalArgumentException("Unknown ViewModel class.")
    }
}


