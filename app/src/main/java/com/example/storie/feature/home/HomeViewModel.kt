package com.example.storie.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storie.core.DataStoreManager
import com.example.storie.data.Result
import com.example.storie.data.remote.mapper.toModel
import com.example.storie.domain.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val appRepository: AppRepository,
    private val dataStoreManager: DataStoreManager,
) : ViewModel() {
    private val _viewEffect = MutableSharedFlow<HomeViewEffect>()
    val viewEffect get() = _viewEffect.asSharedFlow()

    private val _viewState = MutableStateFlow(HomeViewState())
    val viewState get() = _viewState.asStateFlow()

    init {
        viewModelScope.launch {
            _viewState.update {
                it.copy(
                    username = dataStoreManager.getUsername().first()
                )
            }
        }
        getStories()
    }

    fun onEvent(event: HomeViewEvent) {
        when (event) {
            HomeViewEvent.OnRetry -> {
                getStories()
            }

            HomeViewEvent.OnLogout -> {
                viewModelScope.launch {
                    dataStoreManager.clearDataStore()
                    _viewEffect.emit(HomeViewEffect.OnLogout)
                }
            }
        }
    }

    private fun getStories() {
        viewModelScope.launch {
            appRepository.getStories().collectLatest { result ->
                when (result) {
                    is Result.Loading -> {
                        _viewEffect.emit(HomeViewEffect.OnLoading)
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
                            HomeViewEffect.OnSuccess(
                                result.data.message ?: "Get Stories Success"
                            )
                        )
                    }

                    is Result.Error -> {
                        _viewEffect.emit(HomeViewEffect.OnError(result.error))
                    }
                }
            }
        }
    }
}