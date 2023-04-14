package com.projects.foodace.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.projects.foodace.model.Food
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [(User::class), (Food::class)], version = 2)
internal abstract class FoodAceDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun foodDao(): FoodDao

    companion object {
        private var instance: FoodAceDatabase? = null

        fun getDatabase(context: Context): FoodAceDatabase? {
            if (instance == null) {
                synchronized(FoodAceDatabase::class) {
                    if (instance == null) {
                        instance = Room
                            .databaseBuilder(
                            context.applicationContext,
                            FoodAceDatabase::class.java,
                            "database"
                            )
                            .fallbackToDestructiveMigration()
                            .addCallback(Populator())
                            .build()
                    }
                }
            }
            return instance
        }
    }

    private class Populator : Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            instance?.let {
                Log.i("DATABASE", "Pre-populating database...")
                CoroutineScope(Dispatchers.IO).launch {
                    it.userDao().insertUser(
                        User("gabri", "mail", "pass")
                    ) //todo: popolazione
                    it.foodDao().insertFoods(Food.popularFoodsList)
                }
            }
        }
    }
}