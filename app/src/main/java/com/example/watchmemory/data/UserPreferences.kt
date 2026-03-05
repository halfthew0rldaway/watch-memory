package com.example.watchmemory.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

class UserPreferences(private val context: Context) {
    companion object {
        private val USER_NAME_KEY = stringPreferencesKey("user_name")
        private val USER_TITLE_KEY = stringPreferencesKey("user_title")
        private val IS_FIRST_LAUNCH_KEY = androidx.datastore.preferences.core.booleanPreferencesKey("is_first_launch")
    }

    val userName: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[USER_NAME_KEY] ?: "ME"
        }

    val userTitle: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[USER_TITLE_KEY] ?: "WATCHER"
        }

    val isFirstLaunch: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[IS_FIRST_LAUNCH_KEY] ?: true
        }

    suspend fun saveUserName(name: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_NAME_KEY] = name
            preferences[IS_FIRST_LAUNCH_KEY] = false
        }
    }

    suspend fun saveUserTitle(title: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_TITLE_KEY] = title
        }
    }

    suspend fun completeFirstLaunch() {
        context.dataStore.edit { preferences ->
            preferences[IS_FIRST_LAUNCH_KEY] = false
        }
    }
}
