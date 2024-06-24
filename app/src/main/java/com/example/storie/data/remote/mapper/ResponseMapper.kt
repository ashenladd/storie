package com.example.storie.data.remote.mapper

import com.example.storie.data.local.entity.StoryEntity
import com.example.storie.data.remote.response.StoryResponseItem
import com.example.storie.domain.model.StoryModel


fun StoryResponseItem.toModel() = StoryModel(
    photoUrl = photoUrl ?: "",
    createdAt = createdAt ?: "",
    name = name ?: "",
    description = description ?: "",
    lon = lon ?: 0.0,
    id = id,
    lat = lat ?: 0.0
)

fun StoryResponseItem.toEntity() = StoryEntity(
    photoUrl = photoUrl,
    createdAt = createdAt,
    name = name,
    description = description,
    lon = lon,
    id = id,
    lat = lat
)

fun StoryEntity.toModel() = StoryModel(
    photoUrl = photoUrl.orEmpty(),
    createdAt = createdAt.orEmpty(),
    name = name.orEmpty(),
    description = description.orEmpty(),
    lon = lon ?: 0.0,
    id = id,
    lat = lat ?: 0.0
)