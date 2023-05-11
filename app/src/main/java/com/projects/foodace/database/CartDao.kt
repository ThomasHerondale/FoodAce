package com.projects.foodace.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.projects.foodace.model.Food
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCartEntry(entry: UserCartEntry)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCartEntries(entries: List<UserCartEntry>)

    @Query("""
        SELECT * 
        FROM Cart JOIN Food ON Cart.name = Food.name
        WHERE Cart.username = :username
    """)
    fun getCart(username: String): Flow<List<UserCartEntry>>

    @Query("""
        DELETE FROM Cart WHERE Cart.username = :username
    """)
    fun clearCart(username: String)
}