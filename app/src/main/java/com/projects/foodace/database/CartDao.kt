package com.projects.foodace.database

import androidx.room.Dao
import androidx.room.Query

@Dao
interface CartDao {

    @Query("""
        INSERT OR REPLACE INTO Cart VALUES (:username, :foodName, :quantity)
    """)
    fun insertCartEntry(username: String, foodName: String, quantity: Int)
}