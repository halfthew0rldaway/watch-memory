package com.example.watchmemory.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.watchmemory.data.ShowEntity
import com.example.watchmemory.data.ShowRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class EditUiState(
    val title: String = "",
    val episode: String = "1",
    val totalEpisodes: String = "",
    val category: String = "Series",
    val status: String = "Watching",
    val note: String = "",
    val isWatched: Boolean = false,
    val isSaving: Boolean = false,
    val isLoading: Boolean = true,
    val isNew: Boolean = true,
    val saveComplete: Boolean = false
)

class EditViewModel(
    private val repository: ShowRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val showId: Long = savedStateHandle.get<Long>("id") ?: -1L

    private val _uiState = MutableStateFlow(EditUiState())
    val uiState: StateFlow<EditUiState> = _uiState.asStateFlow()

    init {
        if (showId > 0) {
            viewModelScope.launch {
                val show = repository.getShowById(showId)
                if (show != null) {
                    _uiState.update {
                        it.copy(
                            title = show.title,
                            episode = show.episode.toString(),
                            totalEpisodes = show.totalEpisodes?.toString() ?: "",
                            category = show.category,
                            status = show.status,
                            note = show.note,
                            isWatched = show.episode >= (show.totalEpisodes ?: 1),
                            isLoading = false,
                            isNew = false
                        )
                    }
                } else {
                    _uiState.update { it.copy(isLoading = false) }
                }
            }
        } else {
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun updateTitle(title: String) {
        _uiState.update { it.copy(title = title) }
    }

    fun updateEpisode(episode: String) {
        if (episode.isEmpty() || episode.all { it.isDigit() }) {
            _uiState.update { it.copy(episode = episode) }
        }
    }

    fun updateTotalEpisodes(total: String) {
        if (total.isEmpty() || total.all { it.isDigit() }) {
            _uiState.update { it.copy(totalEpisodes = total) }
        }
    }

    fun updateCategory(category: String) {
        _uiState.update { it.copy(category = category) }
    }

    fun updateStatus(status: String) {
        _uiState.update { it.copy(status = status) }
    }

    fun updateNote(note: String) {
        if (note.length <= 40) {
            _uiState.update { it.copy(note = note) }
        }
    }

    fun updateWatched(watched: Boolean) {
        _uiState.update { it.copy(isWatched = watched) }
        if (watched) {
            val totalStr = _uiState.value.totalEpisodes
            val total = if (totalStr.isBlank()) 1 else totalStr.toIntOrNull() ?: 1
            _uiState.update { it.copy(episode = total.toString(), totalEpisodes = total.toString()) }
        } else {
            // No changes to episode/total when un-checking
        }
    }

    fun incrementEpisode() {
        val current = _uiState.value.episode.toIntOrNull() ?: 0
        _uiState.update { it.copy(episode = (current + 1).toString()) }
    }

    fun decrementEpisode() {
        val current = _uiState.value.episode.toIntOrNull() ?: 1
        if (current > 1) {
            _uiState.update { it.copy(episode = (current - 1).toString()) }
        }
    }

    fun saveShow() {
        val currentState = _uiState.value
        val title = currentState.title.trim()
        val episode = currentState.episode.toIntOrNull() ?: 0
        val totalEpisodes = currentState.totalEpisodes.toIntOrNull()
        val note = currentState.note.trim()

        if (title.isBlank()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            val entity = ShowEntity(
                id = if (showId > 0) showId else 0,
                title = title,
                episode = episode,
                totalEpisodes = totalEpisodes,
                category = currentState.category,
                status = currentState.status,
                note = note,
                lastUpdated = System.currentTimeMillis()
            )
            
            if (showId > 0) {
                repository.updateShow(entity)
            } else {
                repository.insertShow(entity)
            }
            _uiState.update { it.copy(isSaving = false, saveComplete = true) }
        }
    }
}

class EditViewModelFactory(private val repository: ShowRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(EditViewModel::class.java)) {
            val savedStateHandle = extras.createSavedStateHandle()
            @Suppress("UNCHECKED_CAST")
            return EditViewModel(repository, savedStateHandle) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
