package com.pbh.androidbase.core.ui

import com.pbh.androidbase.domain.model.DomainError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import java.io.IOException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@OptIn(ExperimentalCoroutinesApi::class)
class BaseViewModelTest {
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
    fun `default launch handler fails loud for unexpected exceptions`() {
        assertFailsWith<IllegalStateException> {
            runTest(dispatcher) {
                val viewModel = TestViewModel()

                viewModel.throwUnexpected()
                advanceUntilIdle()
            }
        }
    }

    @Test
    fun `explicit launch handler receives mapped domain error`() =
        runTest(dispatcher) {
            val viewModel = TestViewModel()
            var handledError: DomainError? = null

            viewModel.throwWithHandler { handledError = it }
            advanceUntilIdle()

            assertEquals(DomainError.Offline, handledError)
        }
}

private class TestViewModel : BaseViewModel<Unit, Nothing>(Unit) {
    fun throwUnexpected() {
        launch {
            throw IOException("offline")
        }
    }

    fun throwWithHandler(onError: (DomainError) -> Unit) {
        launch(onError = onError) {
            throw IOException("offline")
        }
    }
}
