package com.projects.foodace.database

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.projects.foodace.model.CartEntry
import com.projects.foodace.model.Food
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class FoodAceRepository(application: Application) {
    private var userDao: UserDao?
    private var foodDao: FoodDao?
    private var cartDao: CartDao?
    private val coroutineScope = CoroutineScope(Dispatchers.IO)


    init {
        val database = FoodAceDatabase.getDatabase(application)
        userDao = database?.userDao()
        foodDao = database?.foodDao()
        cartDao = database?.cartDao()
    }

    // User methods

    suspend fun checkCredentials(username: String, password: String): Boolean {
        return asyncCheckCredentials(username, password).await()
    }

    private fun asyncCheckCredentials(username: String, password: String): Deferred<Boolean> {
         return coroutineScope.async {
             val user = userDao!!.getUser(username, password)
            return@async user != null
        }
    }

    suspend fun getUser(username: String): User {
        return asyncGetUser(username).await()!!
    }

    private fun asyncGetUser(username: String): Deferred<User?> {
        return coroutineScope.async {
            return@async userDao!!.getUser(username)
        }
    }

    // Food methods

    fun getPopularFoods(): Flow<List<Food>> {
        return foodDao!!.getPopularFoods()
    }

    fun searchFoods(query: String): Flow<List<Food>> {
        val dbQuery = "%$query%"
        return foodDao!!.searchFoods(dbQuery)
    }

    suspend fun getFood(name: String): LiveData<Food> {
        println("called get")
        return asyncGetFood(name).await().map {
            println(it)
            it ?: throw IllegalStateException("Food not found to show details.")
        }
    }

    fun getFavoriteFoods(): Flow<List<Food>> {
        return foodDao!!.getFavoriteFoods()
    }

    private fun asyncGetFood(name: String): Deferred<LiveData<Food?>> {
        return coroutineScope.async {
            return@async foodDao!!.getFood(name)
        }
    }

    // Cart methods

    fun storeCart(username: String, cartContent: List<CartEntry>) {
        coroutineScope.launch {
            cartDao!!.clearCart(username)
            cartDao!!.insertCartEntries(cartContent.map { UserCartEntry(it, username) })
        }
    }

    fun restoreCart(username: String): Flow<List<CartEntry>> {
        return cartDao!!.getCart(username).map { userCartEntries ->
            userCartEntries.map { CartEntry(it) }
        }
    }

    fun setFavorite(food: Food) {
        food.isFavorite = !food.isFavorite
        coroutineScope.launch {
            foodDao!!.updateFood(food)
        }
    }
}