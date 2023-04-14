package com.projects.foodace.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.projects.foodace.R
import com.projects.foodace.model.Category.BURGER
import com.projects.foodace.model.Category.DESSERT
import com.projects.foodace.model.Category.DRINK
import com.projects.foodace.model.Category.PIZZA
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "Food")
data class Food(
    @PrimaryKey
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "category")
    val category: Category,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "price")
    val price: Double,
    @ColumnInfo(name = "imgId")
    val img: Int,
    @ColumnInfo(name = "isPopular")
    val isPopular: Boolean
) : Parcelable {
    companion object {
        val popularFoodsList = listOf(
            Food("Margherita", PIZZA, "Pomodoro, mozzarella, origano",
                7.5, R.drawable.pizza, true),
            Food("Cheeseburger", BURGER, "Burger bun, hamburger, cheddar, insalata",
            7.5, R.drawable.pizza, true),
            Food("Coca Cola", DRINK, "Coca Cola & ice",
                2.5, R.drawable.pizza, true),
            Food("Donut", DESSERT, "Strawberry glazed donut",
                2.0, R.drawable.pizza, true)
        )
    }
}