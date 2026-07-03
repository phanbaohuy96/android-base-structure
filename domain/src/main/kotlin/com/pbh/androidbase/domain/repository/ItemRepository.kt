package com.pbh.androidbase.domain.repository

import com.pbh.androidbase.domain.entity.Item
import com.pbh.androidbase.domain.model.AppResult
import kotlinx.coroutines.flow.Flow

interface ItemRepository {
    fun observeItems(): Flow<List<Item>>

    suspend fun refreshItems(): AppResult<Unit>

    suspend fun getItem(id: String): AppResult<Item>
}
