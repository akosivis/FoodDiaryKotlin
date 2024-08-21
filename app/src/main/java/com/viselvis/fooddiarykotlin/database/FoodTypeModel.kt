package com.viselvis.fooddiarykotlin.database

import android.graphics.drawable.Drawable
import androidx.compose.ui.graphics.vector.ImageVector

data class FoodTypeModel(
    var foodTypeId: Int,
    var foodTypeTitle: String,
    var foodTypeIcon: String,
)

data class SelectFoodTypeModel(
    var foodTypeId: Int,
    var foodTypeTitle: String,
    var contentDescription: String,
    var drawableResource: Int
)

/***
 * foodTypeId
 *
 * 0 - food item
 * 1 - medicine item
 *
 */