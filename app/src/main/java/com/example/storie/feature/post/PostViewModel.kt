package com.example.storie.feature.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storie.data.Result
import com.example.storie.domain.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val appRepository: AppRepository,
) : ViewModel() {
    private val _viewState = MutableStateFlow(PostViewState())
    val viewState = _viewState.asStateFlow()

    private val _viewEffect = MutableSharedFlow<PostViewEffect>()
    val viewEffect = _viewEffect.asSharedFlow()

    fun onEvent(event: PostViewEvent) {
        when (event) {
            is PostViewEvent.OnPickMedia -> {
                _viewState.update {
                    it.copy(uri = event.uri)
                }
            }

            is PostViewEvent.OnUpload -> {
                postStory(
                    event.file,
                    event.description
                )
            }

            is PostViewEvent.OnLocation -> {
                _viewState.update {
                    it.copy(lat = event.lat, lon = event.lon)
                }
            }
        }
    }

    private fun postStory(file: File, desc: String) {
        viewModelScope.launch {
            appRepository.postStory(
                file = file,
                description = desc,
                lat = viewState.value.lat,
                lon = viewState.value.lon,
            ).collectLatest { result ->
                when (result) {
                    is Result.Loading -> {
                        _viewEffect.emit(PostViewEffect.OnLoading)
                    }

                    is Result.Error -> {
                        _viewEffect.emit(PostViewEffect.OnError(result.error))
                    }

                    is Result.Success -> {
                        _viewEffect.emit(
                            PostViewEffect.OnSuccess(
                                result.data.message ?: "Post Success"
                            )
                        )
                    }

                }
            }
        }
    }
}