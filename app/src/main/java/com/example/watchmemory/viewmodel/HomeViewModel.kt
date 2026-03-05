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
    val userName: String = "ME",
    val currentFilter: HomeFilter = HomeFilter.ALL,
    val currentSort: HomeSort = HomeSort.LATEST
)

enum class HomeFilter { ALL, WATCHING, COMPLETED }
enum class HomeSort { AZ, LATEST }

class HomeViewModel(
    private val repository: com.example.watchmemory.data.ShowRepository,
    private val userPreferences: com.example.watchmemory.data.UserPreferences
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _filter = MutableStateFlow(HomeFilter.ALL)
    private val _sort = MutableStateFlow(HomeSort.LATEST)

    val uiState: StateFlow<HomeUiState> = combine(
        repository.getAllShows(),
        _searchQuery,
        _filter,
        _sort,
        userPreferences.userName
    ) { shows, query, filter, sort, name ->
        var filtered = if (query.isBlank()) {
            shows
        } else {
            shows.filter { it.title.contains(query, ignoreCase = true) }
        }

        // Apply Status Filter
        filtered = when (filter) {
            HomeFilter.ALL -> filtered
            HomeFilter.WATCHING -> filtered.filter { show ->
                val isMovie = show.category == "Movie"
                val hasTotal = show.totalEpisodes != null && show.totalEpisodes!! > 0
                if (isMovie) {
                    if (hasTotal) show.episode < show.totalEpisodes!! else true // If no total, assume ongoing
                } else {
                    if (hasTotal) show.episode < show.totalEpisodes!! else true
                }
            }
            HomeFilter.COMPLETED -> filtered.filter { show ->
                val hasTotal = show.totalEpisodes != null && show.totalEpisodes!! > 0
                hasTotal && show.episode >= show.totalEpisodes!!
            }
        }

        // Apply Sorting
        filtered = when (sort) {
            HomeSort.AZ -> filtered.sortedBy { it.title.lowercase() }
            HomeSort.LATEST -> filtered.sortedByDescending { it.lastUpdated }
        }
        
        HomeUiState(
            shows = filtered,
            searchQuery = query,
            totalEpisodesWatched = shows.sumOf { it.episode },
            seriesCount = shows.count { it.category == "Series" },
            animeCount = shows.count { it.category == "Anime" },
            userName = name,
            currentFilter = filter,
            currentSort = sort
        )
    }
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HomeUiState())

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun updateFilter(filter: HomeFilter) {
        _filter.value = filter
    }

    fun updateSort(sort: HomeSort) {
        _sort.value = sort
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
