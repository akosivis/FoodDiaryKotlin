package com.viselvis.fooddiarykotlin.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.viselvis.fooddiarykotlin.database.FoodItemRepository
import java.lang.IllegalArgumentException

class EnterUsernameViewModel: ViewModel() {
    var userName by mutableStateOf("")

    fun enterUserName(input: String) {
        userName = input
    }

    fun saveUserName() {

    }

    suspend fun writeUserName(input: String) {

    }
}

class EnterUsernameViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EnterUsernameViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EnterUsernameViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}