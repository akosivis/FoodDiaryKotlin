package com.viselvis.fooddiarykotlin.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "food_item_table")
data class FoodItemModel(
        @PrimaryKey(autoGenerate = true) val foodItemId: Long,
        var foodItemTitle: String,
        var foodItemDetails: String,
        var foodItemCreated: Long,
        var foodItemLastModified: Long
) {
    constructor(
            foodItemTitle: String,
            foodItemDetails: String,
            foodItemCreated: Long,
            foodItemLastModified: Long
    ) : this(0, foodItemTitle, foodItemDetails, foodItemCreated, foodItemLastModified)
}