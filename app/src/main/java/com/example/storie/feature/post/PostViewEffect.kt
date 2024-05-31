package com.example.storie.feature.post

sealed class PostViewEffect {
    data object OnLoading : PostViewEffect()
    data class OnError(val message: String) : PostViewEffect()
    data class OnSuccess(val message: String) : PostViewEffect()
}