package com.example.storie.feature.home

sealed class HomeViewEffect {
    data object OnLoading : HomeViewEffect()
    data class OnSuccess(val message: String) : HomeViewEffect()
    data class OnError(val message: String) : HomeViewEffect()
    data object OnLogout : HomeViewEffect()
}