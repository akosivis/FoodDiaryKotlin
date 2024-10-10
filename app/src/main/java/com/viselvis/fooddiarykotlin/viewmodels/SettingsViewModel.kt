package com.viselvis.fooddiarykotlin.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.viselvis.fooddiarykotlin.database.FoodItemRepository
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.Calendar

data class SettingsUiState(
    val isLoading: Boolean = true,
    val savedReminderTime: String = "2030",
    val isSetToDarkTheme : Boolean = false,
)

class SettingsViewModel(): ViewModel() {
    private val currentSettingsUiState = MutableStateFlow(
        SettingsUiState()
    )
    val uiState = currentSettingsUiState
}

class SettingsViewModelFactory  : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SettingsViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
