package com.pbh.androidbase.data.remote.dto

import kotlinx.serialization.Serializable

/** Network representation of a home item. */
@Serializable
data class ItemDto(
    val id: String,
    val title: String,
    val description: String,
    val imageUrl: String? = null,
    val updatedAtMillis: Long,
)
