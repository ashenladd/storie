package com.example.storie.feature.post

import android.net.Uri

data class PostViewState(
    val uri: Uri? = null,
    val lat: Double? = null,
    val lon: Double? = null,
)