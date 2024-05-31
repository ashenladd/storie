package com.example.storie.feature.home

import com.example.storie.domain.model.StoryModel

data class HomeViewState(
    val username: String = "",
    val listStory: List<StoryModel> = emptyList(),
)