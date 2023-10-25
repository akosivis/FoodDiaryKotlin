package com.viselvis.fooddiarykotlin.database

data class FoodTypeModel(
    var foodTypeId: Int,
    var foodTypeTitle: String,
    var foodTypeIcon: String,
)

/***
 * foodTypeId
 *
 * 0 - food item
 * 1 - medicine item
 *
 */