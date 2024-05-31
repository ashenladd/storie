package com.example.storie.feature.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storie.core.DataStoreManager
import com.example.storie.data.Result
import com.example.storie.domain.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val appRepository: AppRepository,
    private val dataStoreManager: DataStoreManager,
) : ViewModel() {
    private val _viewEffect = MutableSharedFlow<LoginViewEffect>()
    val viewEffect get() = _viewEffect.asSharedFlow()

    init {
        val token = runBlocking { dataStoreManager.getToken().first() }
        Log.d(
            "LoginViewModelToken",
            "Token: $token"
        )
    }

    fun onEvent(event: LoginViewEvent) {
        when (event) {
            is LoginViewEvent.Login -> {
                login(
                    event.email,
                    event.password
                )
            }
        }
    }

    private fun login(email: String, password: String) {
        viewModelScope.launch {
            appRepository.login(
                email,
                password
            ).collectLatest { result ->
                when (result) {
                    is Result.Loading -> {
                        _viewEffect.emit(LoginViewEffect.OnLoading)
                    }

                    is Result.Success -> {
                        dataStoreManager.setToken(result.data.loginResult?.token.orEmpty())
                        dataStoreManager.setUsername(result.data.loginResult?.name.orEmpty())
                        _viewEffect.emit(
                            LoginViewEffect.OnSuccess(
                                result.data.message ?: "Login Success"
                            )
                        )
                    }

                    is Result.Error -> {
                        _viewEffect.emit(LoginViewEffect.OnError(result.error))
                    }
                }
            }
        }
    }

}