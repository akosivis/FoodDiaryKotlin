package com.viselvis.fooddiarykotlin.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.viselvis.fooddiarykotlin.database.FoodItemModel
import com.viselvis.fooddiarykotlin.database.FoodItemRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ItemDetailViewModel(private val repo: FoodItemRepository) : ViewModel() {
    private var _uiState = MutableStateFlow<ItemDetailUiState>(
        ItemDetailUiState.Loading
    )
    val uiState: StateFlow<ItemDetailUiState> = _uiState.asStateFlow()
    private var _addEditItemState = MutableStateFlow(AddItemUiState())
    val addEditItemState: StateFlow<AddItemUiState> = _addEditItemState.asStateFlow()

    fun fetchFoodItem(itemId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getFoodItemOnGivenId(itemId).apply {
                if (this != null) {
                    _uiState.value = ItemDetailUiState.ViewMode(this)
                } else {
                    _uiState.value = ItemDetailUiState.Error("We cannot load the item!")
                }
            }
        }
    }

    fun editFoodItem(itemId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getFoodItemOnGivenId(itemId).apply {
                if (this != null) {
                    _uiState.value = ItemDetailUiState.EditMode(
                        AddItemUiState(
                            itemName = this.foodItemTitle,
                            itemDetail = this.foodItemDetails,
                            itemIngredientsList = this.foodItemIngredients,
                            itemFoodType = this.foodItemType
                        )
                    )
                } else {
                    _uiState.value = ItemDetailUiState.Error("We cannot load the item!")
                }
            }
        }
    }

    fun insertItemIngredientInput(input: String) {
        if (input.trim().isNotEmpty()) {
            _addEditItemState.update {
                it.copy(
                    itemIngredientInput = input
                )
            }
        }
    }

    fun insertIngredient(ingredientInput: String) {
        if (ingredientInput.trim().isNotEmpty()) {
            if (_uiState.value is ItemDetailUiState.EditMode) {
                var ingredientList = (_uiState.value as ItemDetailUiState.EditMode).itemToEdit.itemIngredientsList + ingredientInput
                _uiState.update {
                    (it as ItemDetailUiState.EditMode).copy(
                        itemToEdit = AddItemUiState(
                            itemName = it.itemToEdit.itemName,
                            itemDetail = it.itemToEdit.itemDetail,
                            errorMessage = it.itemToEdit.errorMessage,
                            isDataInserted = it.itemToEdit.isDataInserted,
                            itemIngredientsList = ingredientList,
                            itemIngredientInput = ""
                        ),
                    )
                }
            }
        }
    }

    fun updateItemName(input: String) {
        if (input.trim().isNotEmpty()) {
            _uiState.update {
                (it as ItemDetailUiState.EditMode).copy(
                    itemToEdit = AddItemUiState(
                        itemName = input,
                        itemDetail = it.itemToEdit.itemDetail,
                        errorMessage = "",
                        isDataInserted = it.itemToEdit.isDataInserted,
                        itemIngredientsList = it.itemToEdit.itemIngredientsList,
                        itemIngredientInput = it.itemToEdit.itemIngredientInput
                    ),
                )
            }
        }
    }

    fun updateItemDetail(inputDetail: String) {
        if (inputDetail.trim().isNotEmpty()) {
            _uiState.update {
                (it as ItemDetailUiState.EditMode).copy(
                    itemToEdit = AddItemUiState(
                        itemName = it.itemToEdit.itemName,
                        itemDetail = inputDetail,
                        errorMessage = "",
                        isDataInserted = it.itemToEdit.isDataInserted,
                        itemIngredientsList = it.itemToEdit.itemIngredientsList,
                        itemIngredientInput = it.itemToEdit.itemIngredientInput
                    ),
                )
            }
        }
    }
}

data class FoodItemState (
    var foodItemModelToDisplay: FoodItemModel? = null
)

sealed interface ItemDetailUiState {
    data object Loading: ItemDetailUiState
    data class Error(val errorMessage: String? = null): ItemDetailUiState
    data class ViewMode(
        val itemDetailToDisplay: FoodItemModel
    ) : ItemDetailUiState
    data class EditMode(
        val itemToEdit: AddItemUiState
    ) : ItemDetailUiState
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