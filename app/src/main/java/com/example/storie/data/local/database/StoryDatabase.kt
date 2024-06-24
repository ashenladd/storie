package com.example.storie.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.storie.data.local.dao.RemoteKeysDao
import com.example.storie.data.local.dao.StoryDao
import com.example.storie.data.local.entity.RemoteKeys
import com.example.storie.data.local.entity.StoryEntity

@Database(
    entities = [StoryEntity::class, RemoteKeys::class],
    version = 2,
    exportSchema = false
)
abstract class StoryDatabase : RoomDatabase() {
    abstract fun storyDao(): StoryDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {
        const val DATABASE_NAME = "story_db"
    }
}