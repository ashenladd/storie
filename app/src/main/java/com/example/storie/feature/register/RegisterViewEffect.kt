package com.example.storie.feature.register

sealed class RegisterViewEffect {
    data object OnLoading : RegisterViewEffect()
    data class OnSuccess(val message: String) : RegisterViewEffect()
    data class OnError(val message: String) : RegisterViewEffect()
}