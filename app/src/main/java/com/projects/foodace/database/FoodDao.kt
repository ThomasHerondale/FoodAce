package com.projects.foodace.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
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
        WHERE Food.isFavorite
    """)
    fun getFavoriteFoods() : Flow<List<Food>>

    @Query("""
        SELECT *
        FROM Food
        WHERE Food.name = :name
    """)
    fun getFood(name: String) : LiveData<Food?>

    @Query("""
        SELECT *
        FROM Food
        WHERE LOWER(Food.name) LIKE LOWER(:query) AND
              Food.category LIKE :category
    """)
    // Query is the user's query with '%' at start and end
    // LOWER in both strings to ignore case
    fun searchFoods(query: String, category: String) : Flow<List<Food>>

    @Update
    fun updateFood(food: Food)
}