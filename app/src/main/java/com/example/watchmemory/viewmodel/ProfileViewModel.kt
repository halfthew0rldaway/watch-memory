package com.example.watchmemory.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.watchmemory.data.UserPreferences
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProfileViewModel(private val userPreferences: UserPreferences) : ViewModel() {
    val userName: StateFlow<String> = userPreferences.userName
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "ME")

    fun updateName(newName: String) {
        viewModelScope.launch {
            userPreferences.saveUserName(newName)
        }
    }
}

class ProfileViewModelFactory(private val userPreferences: UserPreferences) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProfileViewModel(userPreferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
