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
    val isPopular: Boolean,
    @ColumnInfo(name = "isFavorite")
    val isFavorite: Boolean
) : Parcelable {
    companion object {
        val popularFoodsList = listOf(
            Food("Classic Cheeseburger", BURGER, "Beef patty, cheddar cheese, lettuce, tomato, pickles, sesame seed bun", 10.99, R.drawable.classic_cheeseburger, true, true),
            Food("Margherita Pizza", PIZZA, "Tomato sauce, mozzarella cheese, basil leaves", 12.99, R.drawable.margherita_pizza, true, true),
            Food("Chicago-style Hot Dog", Category.HOTDOG, "All-beef hot dog, mustard, relish, onions, tomato slices, pickle spear, celery salt, poppy seed bun", 6.99, R.drawable.chicago_style_hotdog, true, true),
            Food("Strawberry Milkshake", DRINK, "Fresh strawberries, vanilla ice cream", 5.99, R.drawable.strawberry_milkshake, true, true),
            Food("BBQ Pulled Pork Sandwich", BURGER, "Slow-cooked pulled pork, BBQ sauce, coleslaw, toasted bun", 11.99, R.drawable.pulled_pork_sandwich, true, true),
            Food("Pepperoni Pizza", PIZZA, "Tomato sauce, mozzarella cheese, pepperoni slices", 14.99, R.drawable.pepperoni_pizza, true, true),
            Food("Chocolate Brownie Sundae", DESSERT, "Warm chocolate brownie, vanilla ice cream, chocolate sauce, whipped cream, cherry", 6.99, R.drawable.chocolate_brownie_sundae, true, true),
            Food("Iced Tea", DRINK, "Freshly brewed black tea, served over ice", 2.99, R.drawable.iced_tea, true, true),
            Food("Mushroom and Onion Pizza", PIZZA, "Tomato sauce, mozzarella cheese, sautéed mushrooms, caramelized onions", 15.99, R.drawable.mushroom_onion_pizza, true, true),
            Food("BLT Sandwich", BURGER, "Crispy bacon, lettuce, tomato, mayonnaise, toasted bread", 9.99, R.drawable.blt_sandwich, true, true),
            Food("Veggie Burger", BURGER, "Veggie patty, lettuce, tomato, onion, pickles, sesame seed bun", 10.99, R.drawable.veggie_burger, true, true),
            Food("Coca Cola", DRINK, "Coca Cola, ice", 2.5, R.drawable.coca_cola, true, true),
        )
    }
}