package com.pbh.androidbase.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.pbh.androidbase.data.local.entity.ItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {
    @Query("SELECT * FROM items ORDER BY updatedAtMillis DESC")
    fun observeItems(): Flow<List<ItemEntity>>

    @Query("SELECT * FROM items WHERE id = :id LIMIT 1")
    suspend fun getItem(id: String): ItemEntity?

    @Upsert
    suspend fun upsertAll(items: List<ItemEntity>)
}
