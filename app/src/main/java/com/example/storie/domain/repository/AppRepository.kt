package com.example.storie.domain.repository

import com.example.storie.data.Result
import com.example.storie.data.remote.response.LoginResponse
import com.example.storie.data.remote.response.PostResponse
import com.example.storie.data.remote.response.StoriesResponse
import com.example.storie.data.remote.response.StoryResponse
import kotlinx.coroutines.flow.Flow
import java.io.File

interface AppRepository {
    suspend fun register(
        name: String,
        email: String,
        password: String,
    ): Flow<Result<PostResponse>>

    suspend fun login(email: String, password: String): Flow<Result<LoginResponse>>

    suspend fun getStories(
        page: Int? = null,
        size: Int? = null,
        location: Int? = null,
    ): Flow<Result<StoriesResponse>>

    suspend fun getDetailStory(id: String): Flow<Result<StoryResponse>>

    suspend fun postStory(
        file: File,
        description: String,
        lat: Double? = null,
        lon: Double? = null,
    ): Flow<Result<PostResponse>>
}