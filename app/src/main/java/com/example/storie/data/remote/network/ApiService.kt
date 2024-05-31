package com.example.storie.data.remote.network

import com.example.storie.data.remote.response.LoginResponse
import com.example.storie.data.remote.response.PostResponse
import com.example.storie.data.remote.response.StoriesResponse
import com.example.storie.data.remote.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @FormUrlEncoded
    @POST(NetworkConstant.REGISTER)
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
    ): Response<PostResponse>

    @FormUrlEncoded
    @POST(NetworkConstant.LOGIN)
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String,
    ): Response<LoginResponse>

    @GET(NetworkConstant.STORIES)
    suspend fun getStories(
        @Query("page") page: Int?,
        @Query("size") size: Int?,
        @Query("location") location: Int?,
    ): Response<StoriesResponse>

    @GET(NetworkConstant.DETAIL_STORY)
    suspend fun getDetailStory(
        @Path("id") id: String,
    ): Response<StoryResponse>

    @Multipart
    @POST(NetworkConstant.STORIES)
    suspend fun postStory(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: Double?,
        @Part("lon") lon: Double?,
    ): Response<PostResponse>
}