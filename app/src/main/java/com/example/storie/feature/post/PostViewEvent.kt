package com.example.storie.feature.post

import android.net.Uri
import java.io.File

sealed class PostViewEvent {
    data class OnUpload(val file: File, val description: String) : PostViewEvent()
    data class OnPickMedia(val uri: Uri) : PostViewEvent()
    data class OnLocation(val lat: Double, val lon: Double) : PostViewEvent()
}