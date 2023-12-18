package com.viselvis.fooddiarykotlin.database

import androidx.compose.ui.graphics.vector.ImageVector

data class FoodTypeModel(
    var foodTypeId: Int,
    var foodTypeTitle: String,
    var foodTypeIcon: String,
)

data class SelectFoodTypeModel(
    var foodTypeId: Int,
    var foodTypeTitle: String,
    var foodTypeIcon: ImageVector,
    var contentDescription: String
)

/***
 * foodTypeId
 *
 * 0 - food item
 * 1 - medicine item
 *
 */