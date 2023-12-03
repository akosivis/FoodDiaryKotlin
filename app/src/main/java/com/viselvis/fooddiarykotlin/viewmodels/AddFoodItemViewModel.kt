package com.viselvis.fooddiarykotlin.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.*
import com.viselvis.fooddiarykotlin.database.FoodItemModel
import com.viselvis.fooddiarykotlin.database.FoodItemRepository
import kotlinx.coroutines.launch
import java.util.*

class AddFoodItemViewModel(private val repo: FoodItemRepository) : ViewModel() {
    private var _isDataInserted = MutableLiveData<Long>()
    val isDataInserted: LiveData<Long>
        get() = _isDataInserted

    var itemName by mutableStateOf("")
    var itemDetail by mutableStateOf("")
    var itemIngredientInput by mutableStateOf("")
    var itemIngredientsList by mutableStateOf(arrayListOf<String>())
    var errorMessage by mutableStateOf("")

    fun saveFoodItem(foodItem: FoodItemModel) = viewModelScope.launch {
        _isDataInserted.value = repo.insertFoodItem(foodItem)
    }

    fun insertIngredient(ingredientInput: String) {
        if (ingredientInput.trim().isNotEmpty()) {
            itemIngredientsList.add(ingredientInput.trim())
            itemIngredientInput = ""
        }
    }

    fun insertFoodItemOnDb(foodItemType: Int) {
        if (itemName.isNotEmpty()) {
            val dateCreated = Calendar.getInstance().time
            val dateModified = Calendar.getInstance().time
            val newFoodItem = FoodItemModel(
                foodItemType = foodItemType,
                foodItemTitle = itemName,
                foodItemDetails = itemDetail,
                foodItemCreated = dateCreated,
                foodItemLastModified = dateModified,
                foodItemIngredients = itemIngredientsList
            )

            saveFoodItem(newFoodItem)
        } else {
            errorMessage = when (foodItemType) {
                1 -> "Medicine item name is required!"
                else -> "Food item name is required!"
            }
        }
    }

    fun clearErrorMessage() {
        errorMessage = ""
    }
}

class AddFoodItemViewModelFactory (private val repo: FoodItemRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddFoodItemViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddFoodItemViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}