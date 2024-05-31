package com.example.storie.feature.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storie.data.Result
import com.example.storie.data.remote.mapper.toModel
import com.example.storie.domain.model.StoryModel
import com.example.storie.domain.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: AppRepository,
) : ViewModel() {
    private val _viewEffect = MutableSharedFlow<DetailViewEffect>()
    val viewEffect get() = _viewEffect.asSharedFlow()

    private val _storyState = MutableStateFlow(
        StoryModel(
            id = "",
            createdAt = "",
            name = "",
            description = "",
            photoUrl = "",
            lat = 0.0,
            lon = 0.0
        )
    )
    val storyState get() = _storyState.asStateFlow()

    fun getDetailStory(id: String) {
        viewModelScope.launch {
            repository.getDetailStory(id).collectLatest {
                when (it) {
                    is Result.Loading -> {
                        _viewEffect.emit(DetailViewEffect.OnLoading)
                    }

                    is Result.Success -> {
                        _storyState.value = it.data.story?.toModel() ?: StoryModel(
                            id = "",
                            createdAt = "",
                            name = "",
                            description = "",
                            photoUrl = "",
                            lat = 0.0,
                            lon = 0.0

                        )
                        _viewEffect.emit(DetailViewEffect.OnSuccess(it.data.message ?: "Success"))
                    }

                    is Result.Error -> {
                        _viewEffect.emit(DetailViewEffect.OnError(it.error))
                    }
                }
            }

        }
    }
}