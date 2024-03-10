package com.viselvis.fooddiarykotlin.application

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.preferencesDataStoreFile
import com.viselvis.fooddiarykotlin.database.FoodItemRepository
import com.viselvis.fooddiarykotlin.database.FoodListDatabase
import com.viselvis.fooddiarykotlin.database.UserRepository

class FoodItemListApplication: Application() {
    private val USER: String = "user"

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = USER)
    val database by lazy { FoodListDatabase.getInstance(this) }
    val foodItemsRepo by lazy {
        FoodItemRepository(
            database.foodItemDao()
        )
    }
    val userRepo by lazy {
        UserRepository(
            dataStore
        )
    }


}