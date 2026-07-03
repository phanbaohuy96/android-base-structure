package com.pbh.androidbase.domain.entity

data class Item(
    val id: String,
    val title: String,
    val description: String,
    val imageUrl: String?,
    val updatedAtMillis: Long,
)
