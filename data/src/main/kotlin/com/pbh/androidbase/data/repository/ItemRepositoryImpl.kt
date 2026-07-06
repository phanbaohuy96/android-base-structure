package com.pbh.androidbase.data.repository

import android.content.Context
import com.pbh.androidbase.core.common.DispatcherProvider
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
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

/** Offline-first implementation of [ItemRepository] backed by Room and a remote source. */
class ItemRepositoryImpl
    constructor(
        private val context: Context,
        private val itemDao: ItemDao,
        private val remoteDataSource: ItemRemoteDataSource,
        private val dispatcherProvider: DispatcherProvider,
        private val useSeedFallback: Boolean,
    ) : ItemRepository {
        /** Observes cached items and maps them to domain entities on the IO dispatcher. */
        override fun observeItems(): Flow<List<Item>> =
            itemDao
                .observeItems()
                .map { entities -> entities.map { it.toDomain() } }
                .flowOn(dispatcherProvider.io)

        /** Refreshes Room from remote data, using localized seed items when remote fetch fails. */
        @Suppress("TooGenericExceptionCaught")
        override suspend fun refreshItems(): AppResult<Unit> =
            withContext(dispatcherProvider.io) {
                try {
                    val remoteItems =
                        try {
                            remoteDataSource.getItems()
                        } catch (cancellationException: CancellationException) {
                            throw cancellationException
                        } catch (exception: Exception) {
                            if (useSeedFallback) {
                                seedItems()
                            } else {
                                throw exception
                            }
                        }
                    itemDao.upsertAll(remoteItems.map { it.toEntity() })
                    AppResult.Success(Unit)
                } catch (cancellationException: CancellationException) {
                    throw cancellationException
                } catch (exception: Exception) {
                    AppResult.Failure(NetworkErrorMapper.map(exception))
                }
            }

        /** Reads a cached item by [id], returning [DomainError.NotFound] when absent. */
        override suspend fun getItem(id: String): AppResult<Item> =
            withContext(dispatcherProvider.io) {
                val entity = itemDao.getItem(id) ?: return@withContext AppResult.Failure(DomainError.NotFound)
                AppResult.Success(entity.toDomain())
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
