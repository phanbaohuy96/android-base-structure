package com.pbh.androidbase.feature.home.ui.list

import app.cash.turbine.test
import com.pbh.androidbase.core.ui.UiText
import com.pbh.androidbase.domain.entity.Item
import com.pbh.androidbase.domain.model.AppResult
import com.pbh.androidbase.domain.model.DomainError
import com.pbh.androidbase.domain.repository.AuthRepository
import com.pbh.androidbase.domain.repository.ItemRepository
import com.pbh.androidbase.domain.usecase.GetItemsUseCase
import com.pbh.androidbase.domain.usecase.LogoutUseCase
import com.pbh.androidbase.domain.usecase.RefreshItemsUseCase
import com.pbh.androidbase.feature.home.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
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
import kotlin.test.assertIs

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {
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
    fun `item stream renders content and successful refresh clears refreshing`() =
        runTest(dispatcher) {
            val items = MutableSharedFlow<List<Item>>(replay = 1)
            val repository = FakeItemRepository(items = items)
            val viewModel = createViewModel(repository)

            items.emit(listOf(testItem))
            advanceUntilIdle()

            assertEquals(HomeListUiState.Content(listOf(testItem)), viewModel.uiState.value)

            viewModel.refresh()
            advanceUntilIdle()

            assertEquals(HomeListUiState.Content(listOf(testItem), refreshing = false), viewModel.uiState.value)
            assertEquals(2, repository.refreshCalls)
        }

    @Test
    fun `refresh failure emits localized message effect`() =
        runTest(dispatcher) {
            val repository = FakeItemRepository(refreshResult = AppResult.Failure(DomainError.Offline))
            val viewModel = createViewModel(repository)

            viewModel.effectFlow.test {
                advanceUntilIdle()

                val effect = assertIs<HomeListEffect.ShowMessage>(awaitItem())
                assertEquals(UiText.Resource(R.string.home_error_offline_cache), effect.message)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `logout emits logged out effect`() =
        runTest(dispatcher) {
            val authRepository = FakeAuthRepository()
            val viewModel = createViewModel(authRepository = authRepository)

            viewModel.effectFlow.test {
                viewModel.logout()
                advanceUntilIdle()

                assertEquals(HomeListEffect.LoggedOut, awaitItem())
                assertEquals(1, authRepository.logoutCalls)
                cancelAndIgnoreRemainingEvents()
            }
        }

    private fun createViewModel(
        itemRepository: FakeItemRepository = FakeItemRepository(),
        authRepository: FakeAuthRepository = FakeAuthRepository(),
    ): HomeViewModel =
        HomeViewModel(
            getItemsUseCase = GetItemsUseCase(itemRepository),
            refreshItemsUseCase = RefreshItemsUseCase(itemRepository),
            logoutUseCase = LogoutUseCase(authRepository),
        )
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
    private val items: Flow<List<Item>> = flowOf(listOf(testItem)),
    private val refreshResult: AppResult<Unit> = AppResult.Success(Unit),
) : ItemRepository {
    var refreshCalls = 0

    override fun observeItems(): Flow<List<Item>> = items

    override suspend fun refreshItems(): AppResult<Unit> {
        refreshCalls += 1
        return refreshResult
    }

    override suspend fun getItem(id: String): AppResult<Item> = AppResult.Success(testItem)
}

private class FakeAuthRepository : AuthRepository {
    var logoutCalls = 0

    override fun observeSession(): Flow<com.pbh.androidbase.domain.entity.UserSession?> = flowOf(null)

    override suspend fun login(
        email: String,
        password: String,
    ): AppResult<com.pbh.androidbase.domain.entity.UserSession> = error("Login is not used by HomeViewModel")

    override suspend fun logout() {
        logoutCalls += 1
    }
}
