package com.example.storie.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeys(
    @PrimaryKey
    val id: String,

    val prevKey: Int? = null,

    val nextKey: Int? = null
)
