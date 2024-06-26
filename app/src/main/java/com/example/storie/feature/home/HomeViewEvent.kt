package com.example.storie.feature.home

import androidx.lifecycle.LifecycleOwner

sealed class HomeViewEvent {
    data object OnRetry : HomeViewEvent()
    data object OnLogout : HomeViewEvent()
    data object FetchUsername : HomeViewEvent()
    data class FetchStories(val lifecycleOwner: LifecycleOwner) : HomeViewEvent()
}