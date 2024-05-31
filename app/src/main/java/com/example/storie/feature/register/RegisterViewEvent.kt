package com.example.storie.feature.register

sealed class RegisterViewEvent {
    data class Register(val nama: String, val email: String, val password: String) :
        RegisterViewEvent()
}