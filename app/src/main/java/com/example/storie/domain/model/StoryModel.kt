package com.example.storie.domain.model


data class StoryModel(
	val photoUrl: String,
	val createdAt: String,
	val name: String,
	val description: String,
	val lon: Double,
	val id: String,
	val lat: Double,
)