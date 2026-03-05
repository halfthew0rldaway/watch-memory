package com.example.watchmemory.data

import kotlinx.coroutines.flow.Flow

class ShowRepository(private val showDao: ShowDao) {

    fun getAllShows(): Flow<List<ShowEntity>> = showDao.getAllShows()

    suspend fun getShowById(id: Long): ShowEntity? = showDao.getShowById(id)

    suspend fun insertShow(show: ShowEntity): Long = showDao.insertShow(show)

    suspend fun updateShow(show: ShowEntity) = showDao.updateShow(show)

    suspend fun deleteShow(show: ShowEntity) = showDao.deleteShow(show)

    suspend fun getMostRecentShow(): ShowEntity? = showDao.getMostRecentShow()
}
