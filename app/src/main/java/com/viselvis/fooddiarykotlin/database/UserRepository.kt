package com.viselvis.fooddiarykotlin.database

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

data class UserData(
    val userName: String,
    val hasFinishedWalkthrough: Boolean
)

class UserRepository(
    private val dataStore: DataStore<Preferences>,
) {
    object PreferencesKeys {
        val USERNAME = stringPreferencesKey("username")
        val HAS_FINISHED_WALKTHROUGH = booleanPreferencesKey("hasFinishedWalkthrough")
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
                hasFinishedWalkthrough = preferences[PreferencesKeys.HAS_FINISHED_WALKTHROUGH] ?: false
            )
        }

    suspend fun writeUserName(input: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.USERNAME] = input
            preferences[PreferencesKeys.HAS_FINISHED_WALKTHROUGH] = false
        }
    }

    suspend fun finishWalkthrough() {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.HAS_FINISHED_WALKTHROUGH] = true
        }
    }
}