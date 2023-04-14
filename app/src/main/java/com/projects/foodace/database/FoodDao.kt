package com.projects.foodace.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.projects.foodace.model.Food
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodDao {
    @Insert
    fun insertFoods(foods: List<Food>)

    @Query("""
        SELECT *
        FROM Food
        WHERE Food.isPopular
    """)
    fun getPopularFoods() : Flow<List<Food>>

    @Query("""
        SELECT *
        FROM Food
        WHERE Food.name = :name
    """)
    fun getFood(name: String) : LiveData<Food?>
}