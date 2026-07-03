package com.pbh.androidbase.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class ItemEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val imageUrl: String?,
    val updatedAtMillis: Long,
)
