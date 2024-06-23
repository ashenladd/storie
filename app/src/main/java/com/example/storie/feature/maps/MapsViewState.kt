package com.example.storie.feature.maps

import com.example.storie.domain.model.StoryModel

data class MapsViewState (
    val listStory: List<StoryModel> = emptyList(),
)