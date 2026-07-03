package com.pbh.androidbase.data.repository

import android.content.Context
import com.pbh.androidbase.core.network.NetworkErrorMapper
import com.pbh.androidbase.data.R
import com.pbh.androidbase.data.local.dao.ItemDao
import com.pbh.androidbase.data.mapper.toDomain
import com.pbh.androidbase.data.mapper.toEntity
import com.pbh.androidbase.data.remote.dto.ItemDto
import com.pbh.androidbase.data.remote.source.ItemRemoteDataSource
import com.pbh.androidbase.domain.entity.Item
import com.pbh.androidbase.domain.model.AppResult
import com.pbh.androidbase.domain.model.DomainError
import com.pbh.androidbase.domain.repository.ItemRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ItemRepositoryImpl
    @Inject
    constructor(
        @param:ApplicationContext private val context: Context,
        private val itemDao: ItemDao,
        private val remoteDataSource: ItemRemoteDataSource,
    ) : ItemRepository {
        override fun observeItems(): Flow<List<Item>> = itemDao.observeItems().map { entities -> entities.map { it.toDomain() } }

        override suspend fun refreshItems(): AppResult<Unit> =
            runCatching {
                val remoteItems =
                    runCatching { remoteDataSource.getItems() }
                        .getOrElse { seedItems() }
                itemDao.upsertAll(remoteItems.map { it.toEntity() })
            }.fold(
                onSuccess = { AppResult.Success(Unit) },
                onFailure = { AppResult.Failure(NetworkErrorMapper.map(it)) },
            )

        override suspend fun getItem(id: String): AppResult<Item> {
            val entity = itemDao.getItem(id) ?: return AppResult.Failure(DomainError.NotFound)
            return AppResult.Success(entity.toDomain())
        }

        private fun seedItems(): List<ItemDto> {
            val now = System.currentTimeMillis()
            return listOf(
                ItemDto(
                    id = "architecture",
                    title = context.getString(R.string.seed_item_architecture_title),
                    description = context.getString(R.string.seed_item_architecture_description),
                    imageUrl = null,
                    updatedAtMillis = now,
                ),
                ItemDto(
                    id = "offline-cache",
                    title = context.getString(R.string.seed_item_offline_cache_title),
                    description = context.getString(R.string.seed_item_offline_cache_description),
                    imageUrl = null,
                    updatedAtMillis = now - 1_000,
                ),
                ItemDto(
                    id = "agent-ready",
                    title = context.getString(R.string.seed_item_agent_ready_title),
                    description = context.getString(R.string.seed_item_agent_ready_description),
                    imageUrl = null,
                    updatedAtMillis = now - 2_000,
                ),
            )
        }
    }
