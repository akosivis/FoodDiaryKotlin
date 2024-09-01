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

    fun fetchFoodItem(itemId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getFoodItemOnGivenId(itemId).apply {
                if (this != null) {
                    _uiState.value = ItemDetailUiState.Ready(this)
                } else {
                    _uiState.value = ItemDetailUiState.Error("We cannot load the item!")
                }
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
    data class Ready(
        val itemDetailToDisplay: FoodItemModel
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