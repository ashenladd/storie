package com.example.storie.feature.login

sealed class LoginViewEvent {
    data class Login(val email: String, val password: String) : LoginViewEvent()
}