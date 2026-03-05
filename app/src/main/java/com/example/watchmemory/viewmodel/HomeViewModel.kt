package com.example.watchmemory.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.watchmemory.data.ShowEntity
import com.example.watchmemory.data.ShowRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class HomeUiState(
    val shows: List<ShowEntity> = emptyList(),
    val searchQuery: String = "",
    val totalEpisodesWatched: Int = 0,
    val seriesCount: Int = 0,
    val animeCount: Int = 0,
    val userName: String = "ME"
)

class HomeViewModel(
    private val repository: com.example.watchmemory.data.ShowRepository,
    private val userPreferences: com.example.watchmemory.data.UserPreferences
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    val uiState: StateFlow<HomeUiState> = combine(
        repository.getAllShows(),
        _searchQuery,
        userPreferences.userName
    ) { shows, query, name ->
        val filtered = if (query.isBlank()) {
            shows
        } else {
            shows.filter { it.title.contains(query, ignoreCase = true) }
        }
        
        HomeUiState(
            shows = filtered,
            searchQuery = query,
            totalEpisodesWatched = shows.sumOf { it.episode },
            seriesCount = shows.count { it.category == "Series" },
            animeCount = shows.count { it.category == "Anime" },
            userName = name
        )
    }
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HomeUiState())

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun deleteShow(show: ShowEntity) {
        viewModelScope.launch {
            repository.deleteShow(show)
        }
    }

    fun incrementEpisode(show: ShowEntity) {
        viewModelScope.launch {
            val isMovie = show.category == "Movie"
            val max = show.totalEpisodes ?: if (isMovie) 1 else Int.MAX_VALUE
            
            val increment = if (isMovie && show.episode < max) 10 else 1
            var nextEpisode = show.episode + increment
            
            if (nextEpisode > max) nextEpisode = max
            if (show.episode >= max) nextEpisode = 1 // Rewatch loop
            
            repository.updateShow(
                show.copy(
                    episode = nextEpisode,
                    lastUpdated = System.currentTimeMillis()
                )
            )
        }
    }
}

class HomeViewModelFactory(
    private val repository: com.example.watchmemory.data.ShowRepository,
    private val userPreferences: com.example.watchmemory.data.UserPreferences
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(repository, userPreferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
