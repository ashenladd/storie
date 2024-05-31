package com.example.storie.feature.detail

sealed class DetailViewEffect {
    data object OnLoading : DetailViewEffect()
    data class OnError(val message: String) : DetailViewEffect()
    data class OnSuccess(val message: String) : DetailViewEffect()
}