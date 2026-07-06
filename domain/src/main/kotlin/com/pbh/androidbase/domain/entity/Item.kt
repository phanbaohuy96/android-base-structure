package com.pbh.androidbase.domain.entity

/** Item shown in the offline-first home list and detail flow. */
data class Item(
    val id: String,
    val title: String,
    val description: String,
    val imageUrl: String?,
    val updatedAtMillis: Long,
)
