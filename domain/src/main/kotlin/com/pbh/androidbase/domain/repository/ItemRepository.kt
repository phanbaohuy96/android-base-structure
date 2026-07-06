package com.pbh.androidbase.domain.repository

import com.pbh.androidbase.domain.entity.Item
import com.pbh.androidbase.domain.model.AppResult
import kotlinx.coroutines.flow.Flow

/** Domain contract for the offline-first item list and detail store. */
interface ItemRepository {
    /** Observes the locally cached item list. */
    fun observeItems(): Flow<List<Item>>

    /** Refreshes local items from the data source and returns a typed result. */
    suspend fun refreshItems(): AppResult<Unit>

    /** Reads a single cached item by [id], or returns a not-found failure. */
    suspend fun getItem(id: String): AppResult<Item>
}
