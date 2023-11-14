package com.viselvis.fooddiarykotlin.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import java.util.*

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

    @Query("SELECT * FROM food_item_table WHERE foodItemLastModified BETWEEN :startDate AND :endDate")
    fun getFoodItemsByDateRange(startDate: Long, endDate: Long): Flow<List<FoodItemModel>>

    @Query("SELECT * FROM food_item_table WHERE foodItemLastModified BETWEEN :startDate AND :endDate")
    fun getFoodItemsByDate(startDate: Long, endDate: Long): List<FoodItemModel>
//    @Query("SELECT * FROM user WHERE age > :minAge")
//    fun loadAllUsersOlderThan(minAge: Int): Array<User>
//    @Query("SELECT SUM(amount) FROM expense WHERE dob BETWEEN :startDate AND :endDate")
//    fun newAllExpensesFromTo(startDate: Long?, endDate: Long?): Int
}