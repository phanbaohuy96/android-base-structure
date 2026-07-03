package com.pbh.androidbase.data.repository

import com.google.common.truth.Truth.assertThat
import com.pbh.androidbase.core.common.DispatcherProvider
import com.pbh.androidbase.data.local.datastore.SessionStore
import com.pbh.androidbase.data.remote.source.AuthRemoteDataSource
import io.mockk.mockk
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test

class AuthRepositoryImplTest {
    private val dispatcher = StandardTestDispatcher()

    @Test
    fun `login rethrows cancellation`() =
        runTest(dispatcher) {
            val repository =
                AuthRepositoryImpl(
                    remoteDataSource =
                        object : AuthRemoteDataSource {
                            override suspend fun login(
                                email: String,
                                password: String,
                            ) = throw CancellationException("cancelled")
                        },
                    sessionStore = mockk<SessionStore>(),
                    dispatcherProvider = AuthTestDispatcherProvider(dispatcher),
                )

            var cancellationRethrown = false
            try {
                repository.login("demo@example.com", "password")
            } catch (_: CancellationException) {
                cancellationRethrown = true
            }

            assertThat(cancellationRethrown).isTrue()
        }
}

private class AuthTestDispatcherProvider(
    override val io: CoroutineDispatcher,
) : DispatcherProvider {
    override val default: CoroutineDispatcher = io
    override val main: CoroutineDispatcher = io
}
