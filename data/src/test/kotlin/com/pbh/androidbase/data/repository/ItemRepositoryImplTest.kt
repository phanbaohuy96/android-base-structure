package com.pbh.androidbase.data.repository

import android.content.Context
import com.google.common.truth.Truth.assertThat
import com.pbh.androidbase.core.common.DispatcherProvider
import com.pbh.androidbase.data.R
import com.pbh.androidbase.data.local.dao.ItemDao
import com.pbh.androidbase.data.local.entity.ItemEntity
import com.pbh.androidbase.data.remote.source.ItemRemoteDataSource
import com.pbh.androidbase.domain.model.AppResult
import com.pbh.androidbase.domain.model.DomainError
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.io.IOException

class ItemRepositoryImplTest {
    private val dispatcher = StandardTestDispatcher()

    @Test
    fun `refreshItems rethrows cancellation instead of falling back to seeds`() =
        runTest(dispatcher) {
            val remoteDataSource =
                mockk<ItemRemoteDataSource> {
                    coEvery { getItems() } throws CancellationException("cancelled")
                }
            val repository =
                ItemRepositoryImpl(
                    context = mockk<Context>(),
                    itemDao = FakeItemDao(),
                    remoteDataSource = remoteDataSource,
                    dispatcherProvider = ItemTestDispatcherProvider(dispatcher),
                    useSeedFallback = true,
                )

            var cancellationRethrown = false
            try {
                repository.refreshItems()
            } catch (_: CancellationException) {
                cancellationRethrown = true
            }

            assertThat(cancellationRethrown).isTrue()
        }

    @Test
    fun `refreshItems maps remote failure instead of seeding when fallback is disabled`() =
        runTest(dispatcher) {
            val itemDao = FakeItemDao()
            val remoteDataSource =
                mockk<ItemRemoteDataSource> {
                    coEvery { getItems() } throws IOException("offline")
                }
            val repository =
                ItemRepositoryImpl(
                    context = mockk<Context>(),
                    itemDao = itemDao,
                    remoteDataSource = remoteDataSource,
                    dispatcherProvider = ItemTestDispatcherProvider(dispatcher),
                    useSeedFallback = false,
                )

            val result = repository.refreshItems()

            assertThat(result).isEqualTo(AppResult.Failure(DomainError.Offline))
            assertThat(itemDao.upsertedItems).isEmpty()
        }

    @Test
    fun `refreshItems writes localized seeds when fallback is enabled`() =
        runTest(dispatcher) {
            val itemDao = FakeItemDao()
            val context =
                mockk<Context> {
                    every { getString(R.string.seed_item_architecture_title) } returns "Architecture"
                    every { getString(R.string.seed_item_architecture_description) } returns "Layered app structure"
                    every { getString(R.string.seed_item_offline_cache_title) } returns "Offline cache"
                    every { getString(R.string.seed_item_offline_cache_description) } returns "Room-backed content"
                    every { getString(R.string.seed_item_agent_ready_title) } returns "Agent ready"
                    every { getString(R.string.seed_item_agent_ready_description) } returns "Documented workflows"
                }
            val remoteDataSource =
                mockk<ItemRemoteDataSource> {
                    coEvery { getItems() } throws IOException("offline")
                }
            val repository =
                ItemRepositoryImpl(
                    context = context,
                    itemDao = itemDao,
                    remoteDataSource = remoteDataSource,
                    dispatcherProvider = ItemTestDispatcherProvider(dispatcher),
                    useSeedFallback = true,
                )

            val result = repository.refreshItems()

            assertThat(result).isEqualTo(AppResult.Success(Unit))
            assertThat(itemDao.upsertedItems.map { it.id })
                .containsExactly("architecture", "offline-cache", "agent-ready")
        }
}

private class FakeItemDao : ItemDao {
    val upsertedItems = mutableListOf<ItemEntity>()

    override fun observeItems(): Flow<List<ItemEntity>> = flowOf(emptyList())

    override suspend fun getItem(id: String): ItemEntity? = null

    override suspend fun upsertAll(items: List<ItemEntity>) {
        upsertedItems += items
    }
}

private class ItemTestDispatcherProvider(
    override val io: CoroutineDispatcher,
) : DispatcherProvider {
    override val default: CoroutineDispatcher = io
    override val main: CoroutineDispatcher = io
}
