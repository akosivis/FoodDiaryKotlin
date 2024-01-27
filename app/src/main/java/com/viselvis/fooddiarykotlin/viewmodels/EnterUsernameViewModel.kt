package com.viselvis.fooddiarykotlin.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.viselvis.fooddiarykotlin.MainActivity
import com.viselvis.fooddiarykotlin.database.FoodItemModel
import com.viselvis.fooddiarykotlin.database.FoodItemRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException
import java.util.*

class EnterUsernameViewModel(private val repo: FoodItemRepository): ViewModel() {
    private var _uiState = MutableStateFlow(
        EnterUsernameState(
            userName = "",
            isUserNameSaved = false
        )
    )
    val uiState: StateFlow<EnterUsernameState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repo.usernameFlow.collect { name ->
                _uiState.update {
                    it.copy(
                        userName = name,
                        isUserNameSaved = name != ""
                    )
                }
            }
        }
    }

    fun updateUserName(input: String) {
        if (input.trim().isNotEmpty()) {
            _uiState.update {
                it.copy (userName = input)
            }
        }
    }

    fun saveUserName() {
        val userNameTrimmed = _uiState.value.userName.trim()
        if (userNameTrimmed.isNotEmpty()) {
            viewModelScope.launch {
                repo.writeUserName(userNameTrimmed)
            }
            _uiState.update {
                it.copy(isUserNameSaved = true)
            }
        }
    }
}

data class EnterUsernameState (
    var userName: String,
    var isUserNameSaved: Boolean,
)

class EnterUsernameViewModelFactory(private val repo: FoodItemRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EnterUsernameViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EnterUsernameViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}