package com.projects.foodace.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Query("""
       SELECT *
       FROM User
       WHERE User.username = :username AND User.password = :password
    """)
    fun getUser(username: String, password: String): User?

    @Query("""
        SELECT *
        FROM User
        WHERE User.username = :username
    """)
    fun getUser(username: String): User?

    @Insert
    fun insertUser(user: User)
}