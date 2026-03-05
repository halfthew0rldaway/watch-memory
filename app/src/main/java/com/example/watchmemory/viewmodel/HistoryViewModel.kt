package com.example.watchmemory.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.watchmemory.data.ShowEntity
import com.example.watchmemory.data.ShowRepository
import kotlinx.coroutines.flow.Flow

class HistoryViewModel(repository: ShowRepository) : ViewModel() {
    val shows: Flow<List<ShowEntity>> = repository.getAllShows()
}

class HistoryViewModelFactory(private val repository: ShowRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HistoryViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
