package com.example.storie.di

import com.example.storie.data.local.database.StoryDatabase
import com.example.storie.data.remote.network.ApiService
import com.example.storie.data.repository.AppRepositoryImpl
import com.example.storie.domain.repository.AppRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideAppRepository(database: StoryDatabase, apiService: ApiService): AppRepository {
        return AppRepositoryImpl(database, apiService)
    }
}