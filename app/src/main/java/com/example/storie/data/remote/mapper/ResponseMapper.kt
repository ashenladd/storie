package com.example.storie.data.remote.mapper

import com.example.storie.data.remote.response.StoryResponseItem
import com.example.storie.domain.model.StoryModel


fun StoryResponseItem.toModel() = StoryModel(
    photoUrl = photoUrl ?: "",
    createdAt = createdAt ?: "",
    name = name ?: "",
    description = description ?: "",
    lon = lon ?: 0.0,
    id = id ?: "",
    lat = lat ?: 0.0
)