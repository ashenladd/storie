package com.example.storie.feature.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storie.data.Result
import com.example.storie.domain.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val appRepository: AppRepository,
) : ViewModel() {
    private val _viewEffect = MutableSharedFlow<RegisterViewEffect>()
    val viewEffect get() = _viewEffect.asSharedFlow()

    fun onEvent(event: RegisterViewEvent) {
        when (event) {
            is RegisterViewEvent.Register -> {
                register(
                    event.nama,
                    event.email,
                    event.password
                )
            }
        }
    }

    private fun register(nama: String, email: String, password: String) {
        viewModelScope.launch {
            appRepository.register(
                nama,
                email,
                password
            ).collectLatest { result ->
                when (result) {
                    is Result.Loading -> {
                        _viewEffect.emit(RegisterViewEffect.OnLoading)
                    }

                    is Result.Success -> {
                        _viewEffect.emit(
                            RegisterViewEffect.OnSuccess(
                                result.data.message ?: "Register Success"
                            )
                        )
                    }

                    is Result.Error -> {
                        _viewEffect.emit(RegisterViewEffect.OnError(result.error))
                    }
                }
            }
        }
    }
}