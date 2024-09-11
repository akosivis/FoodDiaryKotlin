package com.viselvis.fooddiarykotlin.viewmodels

import androidx.compose.runtime.*
import androidx.lifecycle.*
import com.viselvis.fooddiarykotlin.database.FoodItemModel
import com.viselvis.fooddiarykotlin.database.FoodItemRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

data class AddItemUiState(
    val itemName : String = "",
    val itemDetail : String = "",
    val itemIngredientInput : String = "",
    val itemIngredientsList : List<String> = emptyList(),
    val errorMessage: String = "",
    val isDataInserted: Long = (-1).toLong(),
    val itemFoodType: Int = 0
)

class AddFoodItemViewModel(private val repo: FoodItemRepository) : ViewModel() {

    private var _uiState = MutableStateFlow(AddItemUiState())
    val uiState: StateFlow<AddItemUiState> = _uiState.asStateFlow()

    private var _isDataInserted = MutableLiveData<Long>()
    val isDataInserted: LiveData<Long>
        get() = _isDataInserted

    var itemName by mutableStateOf("")
    var itemDetail by mutableStateOf("")
    var itemIngredientInput by mutableStateOf("")
    val itemIngredientsList = mutableStateListOf<String>()
    var errorMessage by mutableStateOf("")

    fun updateItemName(input: String) {
        if (input.trim().isNotEmpty()) {
            _uiState.update {
                it.copy (
                    itemName = input,
                    errorMessage = ""
                )
            }
        }
    }

    fun updateItemDetail(inputDetail: String) {
        if (inputDetail.trim().isNotEmpty()) {
            _uiState.update {
                it.copy (
                    itemDetail = inputDetail,
                    errorMessage = ""
                )
            }
        }
    }

    fun removeInIngredientList(ingredient: String) {
        val newList = _uiState.value.itemIngredientsList.filter {
            it != ingredient
        }
        _uiState.update {
            it.copy(
                itemIngredientsList = newList
            )
        }
    }

    fun saveFoodItem(foodItem: FoodItemModel) = viewModelScope.launch {
        _uiState.update {
            it.copy(isDataInserted = repo.insertFoodItem(foodItem))
        }
    }

    fun insertIngredient(ingredientInput: String) {
        if (ingredientInput.trim().isNotEmpty()) {

            var ingredientList = _uiState.value.itemIngredientsList + ingredientInput
            _uiState.update {
                it.copy(
                    itemIngredientsList = ingredientList,
                    itemIngredientInput = ""
                )
            }
        }
    }

    fun insertItemIngredientInput(input: String) {
        if (input.trim().isNotEmpty()) {
            _uiState.update {
                it.copy(
                    itemIngredientInput = input
                )
            }
        }
    }

    fun insertFoodItemOnDb(foodItemType: Int) {

        if (_uiState.value.itemName.isNotEmpty()) {

            val foodItemIngredients: ArrayList<String> = arrayListOf()
            _uiState.value.itemIngredientsList.forEach {
               foodItemIngredients.add(it)
            }

            val dateCreated = Calendar.getInstance().time
            val dateModified = Calendar.getInstance().time
            val newFoodItem = FoodItemModel(
                foodItemType = foodItemType,
                foodItemTitle = _uiState.value.itemName,
                foodItemDetails = _uiState.value.itemDetail,
                foodItemCreated = dateCreated,
                foodItemLastModified = dateModified,
                foodItemIngredients = foodItemIngredients
            )

            saveFoodItem(newFoodItem)
        } else {
            _uiState.update {
                it.copy(
                    errorMessage = when (foodItemType) {
                        1 -> "Medicine item name is required!"
                        else -> "Food item name is required!"
                    }
                )
            }
        }
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