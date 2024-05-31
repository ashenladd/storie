package com.example.storie.feature.login

sealed class LoginViewEffect {
    data object OnLoading : LoginViewEffect()
    data class OnSuccess(val message: String) : LoginViewEffect()
    data class OnError(val message: String) : LoginViewEffect()
}