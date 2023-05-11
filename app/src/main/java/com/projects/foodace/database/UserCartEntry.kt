package com.projects.foodace.database

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.projects.foodace.model.CartEntry
import com.projects.foodace.model.Food

@Entity(
    tableName = "Cart",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["username"],
            childColumns = ["username"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Food::class,
            parentColumns = ["name"],
            childColumns = ["name"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    primaryKeys = ["username", "name"],
    indices = [Index("name")]
)
class UserCartEntry (
    @ColumnInfo(name = "username") val username: String,
    @Embedded val food: Food,
    @ColumnInfo(name = "quantity") val quantity: Int
) {
    constructor(entry: CartEntry, username: String) :
            this(username, entry.food, entry.quantity)

    override fun toString(): String {
        return "UserCartEntry(username='$username', food=$food, quantity=$quantity)"
    }
}