package com.example.storie.data.remote.response

import com.google.gson.annotations.SerializedName

data class ApiError(
    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)
