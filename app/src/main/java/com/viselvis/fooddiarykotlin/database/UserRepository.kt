package com.viselvis.fooddiarykotlin.database

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

data class UserData(
    val userName: String,
    val isFirstTimeLogin: Boolean
)

class UserRepository(
    private val dataStore: DataStore<Preferences>,
) {
    object PreferencesKeys {
        val USERNAME = stringPreferencesKey("username")
        val IS_FIRST_TIME_LOGIN = booleanPreferencesKey("isFirstTimeLogin")
    }

    val userFlow: Flow<UserData> = dataStore.data
        .catch { exception ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            UserData (
                userName = preferences[PreferencesKeys.USERNAME] ?: "",
                isFirstTimeLogin = preferences[PreferencesKeys.IS_FIRST_TIME_LOGIN] ?: true
            )
        }

    suspend fun writeUserName(input: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.USERNAME] = input
            preferences[PreferencesKeys.IS_FIRST_TIME_LOGIN] = true
        }
    }

    suspend fun isFirstTimeLogin
}