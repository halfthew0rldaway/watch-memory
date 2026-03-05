package com.example.watchmemory.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ShowEntity::class], version = 2, exportSchema = false)
abstract class WatchMemoryDatabase : RoomDatabase() {
    abstract fun showDao(): ShowDao

    companion object {
        @Volatile
        private var INSTANCE: WatchMemoryDatabase? = null

        fun getInstance(context: Context): WatchMemoryDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WatchMemoryDatabase::class.java,
                    "watch_memory_db"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
