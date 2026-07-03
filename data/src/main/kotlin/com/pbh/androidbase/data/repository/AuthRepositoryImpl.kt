package com.pbh.androidbase.data.repository

import com.pbh.androidbase.core.common.DispatcherProvider
import com.pbh.androidbase.core.network.NetworkErrorMapper
import com.pbh.androidbase.data.local.datastore.SessionStore
import com.pbh.androidbase.data.mapper.toDomain
import com.pbh.androidbase.data.remote.source.AuthRemoteDataSource
import com.pbh.androidbase.domain.entity.UserSession
import com.pbh.androidbase.domain.model.AppResult
import com.pbh.androidbase.domain.repository.AuthRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepositoryImpl
    @Inject
    constructor(
        private val remoteDataSource: AuthRemoteDataSource,
        private val sessionStore: SessionStore,
        private val dispatcherProvider: DispatcherProvider,
    ) : AuthRepository {
        override fun observeSession(): Flow<UserSession?> = sessionStore.observeSession()

        @Suppress("TooGenericExceptionCaught")
        override suspend fun login(
            email: String,
            password: String,
        ): AppResult<UserSession> =
            withContext(dispatcherProvider.io) {
                try {
                    val session = remoteDataSource.login(email, password).toDomain()
                    sessionStore.save(session)
                    AppResult.Success(session)
                } catch (cancellationException: CancellationException) {
                    throw cancellationException
                } catch (exception: Exception) {
                    AppResult.Failure(NetworkErrorMapper.map(exception))
                }
            }

        override suspend fun logout() =
            withContext(dispatcherProvider.io) {
                sessionStore.clear()
            }
    }
