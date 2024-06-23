package com.example.storie.feature.maps

sealed class MapsViewEffect {
    data object OnLoading : MapsViewEffect()
    data class OnSuccess(val message: String) : MapsViewEffect()
    data class OnError(val message: String) : MapsViewEffect()
}