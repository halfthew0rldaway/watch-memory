package com.example.watchmemory.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ShowDao {
    @Query("SELECT * FROM shows ORDER BY lastUpdated DESC")
    fun getAllShows(): Flow<List<ShowEntity>>

    @Query("SELECT * FROM shows WHERE id = :id")
    suspend fun getShowById(id: Long): ShowEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShow(show: ShowEntity): Long

    @Update
    suspend fun updateShow(show: ShowEntity)

    @Delete
    suspend fun deleteShow(show: ShowEntity)

    @Query("SELECT * FROM shows ORDER BY lastUpdated DESC LIMIT 1")
    suspend fun getMostRecentShow(): ShowEntity?

    @Query("SELECT * FROM shows ORDER BY lastUpdated DESC")
    suspend fun getAllShowsList(): List<ShowEntity>
}
