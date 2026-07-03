package com.pbh.androidbase.domain.usecase

import com.pbh.androidbase.domain.entity.Item
import com.pbh.androidbase.domain.model.AppResult
import com.pbh.androidbase.domain.model.DomainError
import com.pbh.androidbase.domain.repository.ItemRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class ItemUseCaseTest {
    @Test
    fun `get items delegates to repository stream`() =
        runTest {
            val repository = FakeItemRepository()
            val result = GetItemsUseCase(repository)().toList()

            assertEquals(listOf(listOf(testItem)), result)
        }

    @Test
    fun `refresh items delegates to repository`() =
        runTest {
            val repository = FakeItemRepository(refreshResult = AppResult.Failure(DomainError.Offline))

            val result = RefreshItemsUseCase(repository)()

            assertEquals(AppResult.Failure(DomainError.Offline), result)
            assertEquals(1, repository.refreshCalls)
        }

    @Test
    fun `get item detail delegates to repository`() =
        runTest {
            val repository = FakeItemRepository()

            val result = GetItemDetailUseCase(repository)("item-1")

            val success = assertIs<AppResult.Success<Item>>(result)
            assertEquals(testItem, success.data)
            assertEquals("item-1", repository.lastRequestedId)
        }
}

private val testItem =
    Item(
        id = "item-1",
        title = "Dashboard",
        description = "Template item",
        imageUrl = null,
        updatedAtMillis = 1_700_000_000_000,
    )

private class FakeItemRepository(
    private val refreshResult: AppResult<Unit> = AppResult.Success(Unit),
    private val detailResult: AppResult<Item> = AppResult.Success(testItem),
) : ItemRepository {
    var refreshCalls = 0
    var lastRequestedId: String? = null

    override fun observeItems(): Flow<List<Item>> = flowOf(listOf(testItem))

    override suspend fun refreshItems(): AppResult<Unit> {
        refreshCalls += 1
        return refreshResult
    }

    override suspend fun getItem(id: String): AppResult<Item> {
        lastRequestedId = id
        return detailResult
    }
}
