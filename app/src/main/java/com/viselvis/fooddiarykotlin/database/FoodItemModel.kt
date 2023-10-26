package com.viselvis.fooddiarykotlin.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*
import kotlin.collections.ArrayList

@Entity(tableName = "food_item_table")
data class FoodItemModel(
        @PrimaryKey(autoGenerate = true) val foodItemId: Long,
        var foodItemType: Int,
        var foodItemTitle: String,
        var foodItemDetails: String,
        var foodItemCreated: Date,
        var foodItemLastModified: Date,
        var foodItemIngredients: ArrayList<String>
) {
    constructor(
            foodItemType: Int,
            foodItemTitle: String,
            foodItemDetails: String,
            foodItemCreated: Date,
            foodItemLastModified: Date,
            foodItemIngredients: ArrayList<String>
    ) : this(0, foodItemType, foodItemTitle, foodItemDetails, foodItemCreated, foodItemLastModified, foodItemIngredients)
}