package com.example.watchmemory.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shows")
data class ShowEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val episode: Int = 1,
    val totalEpisodes: Int? = null,
    val category: String = "Series", // Anime, Series, Movie, etc.
    val status: String = "Watching", // Watching, Finished, Dropped
    val note: String = "",
    val lastUpdated: Long = System.currentTimeMillis()
)
