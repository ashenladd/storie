package com.example.storie.feature.maps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storie.data.Result
import com.example.storie.data.remote.mapper.toModel
import com.example.storie.domain.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(
    private val appRepository: AppRepository,
) : ViewModel() {
    private val _viewEffect = MutableSharedFlow<MapsViewEffect>()
    val viewEffect get() = _viewEffect.asSharedFlow()

    private val _viewState = MutableStateFlow(MapsViewState())
    val viewState get() = _viewState.asStateFlow()

    init {
        getStories()
    }

    fun getStories() {
        viewModelScope.launch {
            appRepository.getStoriesWithLocation().collectLatest { result ->
                when (result) {
                    is Result.Loading -> {
                        _viewEffect.emit(MapsViewEffect.OnLoading)
                    }

                    is Result.Success -> {
                        _viewState.update {
                            it.copy(
                                listStory = result.data.listStory?.map {
                                    it.toModel()
                                } ?: emptyList()
                            )
                        }
                        _viewEffect.emit(
                            MapsViewEffect.OnSuccess(
                                result.data.message ?: "Get Stories Success"
                            )
                        )
                    }

                    is Result.Error -> {
                        _viewEffect.emit(MapsViewEffect.OnError(result.error))
                    }
                }
            }
        }
    }
}