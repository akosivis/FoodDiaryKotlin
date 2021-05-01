package com.viselvis.fooddiarykotlin.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodItemDao {
    @Insert
    suspend fun insertFoodItem(foodItem: FoodItemModel): Long

    @Update
    fun updateFoodItem(foodItem: FoodItemModel)

    @Query("SELECT * FROM food_item_table ORDER BY foodItemCreated DESC")
    fun getAllFoodItems(): Flow<List<FoodItemModel>>

    @Query("SELECT * FROM food_item_table ORDER BY foodItemCreated DESC LIMIT 3")
    fun getFirstThreeFoodItems(): Flow<List<FoodItemModel>>
}