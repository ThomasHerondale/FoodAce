package com.projects.foodace.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.Relation
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
            childColumns = ["foodName"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    primaryKeys = ["username", "foodName"]
)
class UserCartEntry (
    @ColumnInfo(name = "username") val username: String,
    @ColumnInfo(name = "foodName") val foodName: String,
    @ColumnInfo(name = "quantity") val quantity: Int
)