package com.example.storie

import com.example.storie.data.local.entity.StoryEntity

object DummyData {
    fun generateDummyStoriesResponse(): List<StoryEntity> {
        val items: MutableList<StoryEntity> = mutableListOf()
        for (i in 0..100) {
            items.add(
                StoryEntity(
                    id = "story-FvU4u0Vp2S3PMsFg",
                    photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1641623658595_dummy-pic.png",
                    createdAt = "2022-01-08T06:34:18.598Z",
                    name = "Dimas",
                    description = "Lorem Ipsum",
                    lon = -16.002,
                    lat = -10.212,
                ),
            )
        }
        return items
    }
}