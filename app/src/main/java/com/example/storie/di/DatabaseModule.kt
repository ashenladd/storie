package com.example.storie.di

import android.app.Application
import androidx.room.Room
import com.example.storie.data.local.database.StoryDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideStoryDatabase(app: Application): StoryDatabase {
        return Room.databaseBuilder(
            app,
            StoryDatabase::class.java,
            StoryDatabase.DATABASE_NAME
        ).build()
    }
}