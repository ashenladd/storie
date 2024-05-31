package com.example.storie.data.remote.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName


@Keep
data class PostResponse(

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)
