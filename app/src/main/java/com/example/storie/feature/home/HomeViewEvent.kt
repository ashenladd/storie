package com.example.storie.feature.home

sealed class HomeViewEvent {
    data object OnRetry : HomeViewEvent()
    data object OnLogout : HomeViewEvent()
}