package com.pbh.androidbase.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ItemDto(
    val id: String,
    val title: String,
    val description: String,
    val imageUrl: String? = null,
    val updatedAtMillis: Long,
)
