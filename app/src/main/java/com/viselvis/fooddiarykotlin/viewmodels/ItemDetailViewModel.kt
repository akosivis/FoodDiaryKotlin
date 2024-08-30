package com.viselvis.fooddiarykotlin.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.viselvis.fooddiarykotlin.database.FoodItemRepository

class ItemDetailViewModel(private val repo: FoodItemRepository) : ViewModel() {

}

class ItemDetailViewModelFactory (private val repo: FoodItemRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ItemDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ItemDetailViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}