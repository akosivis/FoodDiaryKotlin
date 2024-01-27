package com.viselvis.fooddiarykotlin.application

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.preferencesDataStoreFile
import com.viselvis.fooddiarykotlin.database.FoodItemRepository
import com.viselvis.fooddiarykotlin.database.FoodListDatabase

class FoodItemListApplication: Application() {
    private val USER_NAME: String = "username"

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = USER_NAME)
    val database by lazy { FoodListDatabase.getInstance(this) }
    val repository by lazy {
        FoodItemRepository(
            dataStore,
            database.foodItemDao()
        )
    }


}