package com.example.storie.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.storie.data.Result
import com.example.storie.data.StoryRemoteMediator
import com.example.storie.data.local.database.StoryDatabase
import com.example.storie.data.local.entity.StoryEntity
import com.example.storie.data.remote.network.ApiService
import com.example.storie.data.remote.response.ApiError
import com.example.storie.data.remote.response.LoginResponse
import com.example.storie.data.remote.response.PostResponse
import com.example.storie.data.remote.response.StoriesResponse
import com.example.storie.data.remote.response.StoryResponse
import com.example.storie.domain.repository.AppRepository
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class AppRepositoryImpl @Inject constructor(
    private val database: StoryDatabase,
    private val apiService: ApiService,
) : AppRepository {
    override suspend fun register(
        name: String,
        email: String,
        password: String,
    ): Flow<Result<PostResponse>> {
        return flow {
            val response = apiService.register(
                name,
                email,
                password
            )
            if (response.isSuccessful) {
                emit(Result.Success(response.body() ?: PostResponse()))
            } else {
                val errorBody = response.errorBody()
                val apiError =
                    Gson().fromJson(
                        errorBody?.string() ?: "",
                        ApiError::class.java
                    )
                emit(Result.Error(apiError.message ?: "Unknown error"))
            }

        }.onStart { emit(Result.Loading) }
    }

    override suspend fun login(
        email: String,
        password: String,
    ): Flow<Result<LoginResponse>> {
        return flow {
            val response = apiService.login(
                email,
                password
            )
            if (response.isSuccessful) {
                emit(Result.Success(response.body() ?: LoginResponse()))
            } else {
                val errorBody = response.errorBody()
                val apiError =
                    Gson().fromJson(
                        errorBody?.string() ?: "",
                        ApiError::class.java
                    )
                emit(Result.Error(apiError.message ?: "Unknown error"))
            }
        }.onStart { emit(Result.Loading) }
    }

    override suspend fun getStories(
        page: Int?,
        size: Int?,
        location: Int?,
    ): Flow<Result<StoriesResponse>> {
        return flow {
            try {
                val response = apiService.getStories(
                    page,
                    size,
                    location
                )
                if (response.isSuccessful) {
                    emit(Result.Success(response.body() ?: StoriesResponse()))
                } else {
                    val errorBody = response.errorBody()
                    val apiError =
                        Gson().fromJson(
                            errorBody?.string() ?: "",
                            ApiError::class.java
                        )
                    emit(Result.Error(apiError.message ?: "Unknown error"))
                }
            } catch (e: Exception) {
                emit(Result.Error(e.message ?: "Unknown error"))
            }
        }.onStart { emit(Result.Loading) }
    }

    override suspend fun getStoriesWithLocation(
        page: Int?,
        size: Int?,
        location: Int?,
    ): Flow<Result<StoriesResponse>> {
        return flow {
            try {
                val response = apiService.getStories(
                    page,
                    size,
                    location
                )
                if (response.isSuccessful) {
                    emit(Result.Success(response.body() ?: StoriesResponse()))
                } else {
                    val errorBody = response.errorBody()
                    val apiError =
                        Gson().fromJson(
                            errorBody?.string() ?: "",
                            ApiError::class.java
                        )
                    emit(Result.Error(apiError.message ?: "Unknown error"))
                }
            } catch (e: Exception) {
                emit(Result.Error(e.message ?: "Unknown error"))
            }
        }.onStart { emit(Result.Loading) }
    }

    override fun getStoriesPaging(
        page: Int?,
        size: Int?,
        location: Int?,
    ): LiveData<PagingData<StoryEntity>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 12,
            ),
            remoteMediator = StoryRemoteMediator(
                database,
                apiService
            ),
            pagingSourceFactory = {
                database.storyDao().getAllStory()
            }
        ).liveData
    }


    override suspend fun getDetailStory(
        id: String,
    ): Flow<Result<StoryResponse>> {
        return flow {
            try {
                val response = apiService.getDetailStory(id)
                if (response.isSuccessful) {
                    emit(Result.Success(response.body() ?: StoryResponse()))
                } else {
                    val errorBody = response.errorBody()
                    val apiError =
                        Gson().fromJson(
                            errorBody?.string() ?: "",
                            ApiError::class.java
                        )
                    emit(Result.Error(apiError.message ?: "Unknown error"))
                }
            } catch (e: Exception) {
                emit(Result.Error(e.message ?: "Unknown error"))
            }
        }.onStart { emit(Result.Loading) }
    }

    override suspend fun postStory(
        file: File,
        description: String,
        lat: Double?,
        lon: Double?,
    ): Flow<Result<PostResponse>> {
        return flow {
            try {
                val requestBody = description.toRequestBody("text/plain".toMediaType())
                val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
                val multipartBody = MultipartBody.Part.createFormData(
                    "photo",
                    file.name,
                    requestImageFile
                )

                val response = apiService.postStory(
                    multipartBody,
                    requestBody,
                    lat,
                    lon
                )
                if (response.isSuccessful) {
                    emit(Result.Success(response.body() ?: PostResponse()))
                } else {
                    val errorBody = response.errorBody()
                    val apiError =
                        Gson().fromJson(
                            errorBody?.string() ?: "",
                            ApiError::class.java
                        )
                    emit(Result.Error(apiError.message ?: "Unknown erorr"))
                }
            } catch (e: Exception) {
                emit(Result.Error(e.message ?: "Unknown error"))
            }
        }.onStart { emit(Result.Loading) }
    }

}