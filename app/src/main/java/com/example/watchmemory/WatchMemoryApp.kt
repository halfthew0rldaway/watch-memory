package com.example.watchmemory

import android.app.Application
import com.example.watchmemory.data.ShowRepository
import com.example.watchmemory.data.UserPreferences
import com.example.watchmemory.data.WatchMemoryDatabase

class WatchMemoryApp : Application() {
    val database by lazy { WatchMemoryDatabase.getInstance(this) }
    val repository by lazy { ShowRepository(database.showDao()) }
    val userPreferences by lazy { UserPreferences(this) }
}
