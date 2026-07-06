package com.pbh.androidbase.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.pbh.androidbase.data.local.entity.ItemEntity
import kotlinx.coroutines.flow.Flow

/** Room access contract for cached home items. */
@Dao
interface ItemDao {
    /** Observes all cached items with the newest item first. */
    @Query("SELECT * FROM items ORDER BY updatedAtMillis DESC")
    fun observeItems(): Flow<List<ItemEntity>>

    /** Reads one cached item by [id], or null when absent. */
    @Query("SELECT * FROM items WHERE id = :id LIMIT 1")
    suspend fun getItem(id: String): ItemEntity?

    /** Inserts or updates [items] in the local cache. */
    @Upsert
    suspend fun upsertAll(items: List<ItemEntity>)
}
