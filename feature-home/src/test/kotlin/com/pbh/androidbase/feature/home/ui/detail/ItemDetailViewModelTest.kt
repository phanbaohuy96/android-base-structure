package com.pbh.androidbase.feature.home.ui.detail

import com.pbh.androidbase.core.ui.UiText
import com.pbh.androidbase.domain.entity.Item
import com.pbh.androidbase.domain.model.AppResult
import com.pbh.androidbase.domain.model.DomainError
import com.pbh.androidbase.domain.repository.ItemRepository
import com.pbh.androidbase.domain.usecase.GetItemDetailUseCase
import com.pbh.androidbase.feature.home.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class ItemDetailViewModelTest {
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `load success renders content`() =
        runTest(dispatcher) {
            val viewModel = ItemDetailViewModel(GetItemDetailUseCase(FakeItemRepository()))

            viewModel.load("item-1")
            advanceUntilIdle()

            assertEquals(ItemDetailUiState.Content(testItem), viewModel.uiState.value)
        }

    @Test
    fun `load failure renders localized error`() =
        runTest(dispatcher) {
            val viewModel =
                ItemDetailViewModel(
                    GetItemDetailUseCase(FakeItemRepository(result = AppResult.Failure(DomainError.NotFound))),
                )

            viewModel.load("missing")
            advanceUntilIdle()

            assertEquals(
                ItemDetailUiState.Error(UiText.Resource(R.string.home_error_item_not_found)),
                viewModel.uiState.value,
            )
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
    private val result: AppResult<Item> = AppResult.Success(testItem),
) : ItemRepository {
    override fun observeItems(): Flow<List<Item>> = flowOf(emptyList())

    override suspend fun refreshItems(): AppResult<Unit> = AppResult.Success(Unit)

    override suspend fun getItem(id: String): AppResult<Item> = result
}
