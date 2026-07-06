package com.pbh.androidbase.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/** Room representation of a home item cached for offline reads. */
@Entity(tableName = "items")
data class ItemEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val imageUrl: String?,
    val updatedAtMillis: Long,
)
