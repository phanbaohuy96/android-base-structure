package com.pbh.androidbase.data.repository

import com.pbh.androidbase.core.network.NetworkErrorMapper
import com.pbh.androidbase.data.local.datastore.SessionStore
import com.pbh.androidbase.data.mapper.toDomain
import com.pbh.androidbase.data.remote.source.AuthRemoteDataSource
import com.pbh.androidbase.domain.entity.UserSession
import com.pbh.androidbase.domain.model.AppResult
import com.pbh.androidbase.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthRepositoryImpl
    @Inject
    constructor(
        private val remoteDataSource: AuthRemoteDataSource,
        private val sessionStore: SessionStore,
    ) : AuthRepository {
        override fun observeSession(): Flow<UserSession?> = sessionStore.observeSession()

        override suspend fun login(
            email: String,
            password: String,
        ): AppResult<UserSession> =
            runCatching {
                remoteDataSource.login(email, password).toDomain()
            }.fold(
                onSuccess = { session ->
                    sessionStore.save(session)
                    AppResult.Success(session)
                },
                onFailure = { AppResult.Failure(NetworkErrorMapper.map(it)) },
            )

        override suspend fun logout() {
            sessionStore.clear()
        }
    }
