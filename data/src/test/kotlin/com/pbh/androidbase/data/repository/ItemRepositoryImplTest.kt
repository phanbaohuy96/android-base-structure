package com.pbh.androidbase.data.repository

import android.content.Context
import com.google.common.truth.Truth.assertThat
import com.pbh.androidbase.core.common.DispatcherProvider
import com.pbh.androidbase.data.local.dao.ItemDao
import com.pbh.androidbase.data.local.entity.ItemEntity
import com.pbh.androidbase.data.remote.source.ItemRemoteDataSource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test

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
                )

            var cancellationRethrown = false
            try {
                repository.refreshItems()
            } catch (_: CancellationException) {
                cancellationRethrown = true
            }

            assertThat(cancellationRethrown).isTrue()
        }
}

private class FakeItemDao : ItemDao {
    override fun observeItems(): Flow<List<ItemEntity>> = flowOf(emptyList())

    override suspend fun getItem(id: String): ItemEntity? = null

    override suspend fun upsertAll(items: List<ItemEntity>) = Unit
}

private class ItemTestDispatcherProvider(
    override val io: CoroutineDispatcher,
) : DispatcherProvider {
    override val default: CoroutineDispatcher = io
    override val main: CoroutineDispatcher = io
}
