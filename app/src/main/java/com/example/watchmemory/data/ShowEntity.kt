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
    val category: String = "Series",
    val status: String = "Watching",
    val note: String = "",
    val lastUpdated: Long = System.currentTimeMillis(),
    val imdbId: String = "",
    val posterUrl: String = ""
)
