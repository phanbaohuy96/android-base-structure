package com.pbh.androidbase.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.pbh.androidbase.domain.entity.UserSession
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.sessionDataStore by preferencesDataStore(name = "session")

/** Persists session identity in DataStore and the auth token in encrypted storage. */
@Singleton
class SessionStore
    @Inject
    constructor(
        @param:ApplicationContext private val context: Context,
    ) {
        private val userIdKey = stringPreferencesKey("user_id")
        private val emailKey = stringPreferencesKey("email")
        private val tokenKey = "session_token"

        private val encryptedPreferences by lazy {
            val masterKey =
                MasterKey
                    .Builder(context)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build()

            EncryptedSharedPreferences.create(
                context,
                "secure_session",
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
            )
        }

        /** Observes the reconstructed session, or null when any session part is missing. */
        fun observeSession(): Flow<UserSession?> =
            context.sessionDataStore.data.map { preferences ->
                val userId = preferences[userIdKey]
                val email = preferences[emailKey]
                val token = encryptedPreferences.getString(tokenKey, null)
                if (userId != null && email != null && token != null) {
                    UserSession(userId = userId, email = email, token = token)
                } else {
                    null
                }
            }

        /** Returns the current token synchronously for request interception. */
        fun currentToken(): String? = encryptedPreferences.getString(tokenKey, null)

        /** Saves identity data and token for the authenticated [session]. */
        suspend fun save(session: UserSession) {
            encryptedPreferences.edit().putString(tokenKey, session.token).apply()
            context.sessionDataStore.edit { preferences ->
                preferences[userIdKey] = session.userId
                preferences[emailKey] = session.email
            }
        }

        /** Clears all persisted session state. */
        suspend fun clear() {
            encryptedPreferences.edit().clear().apply()
            context.sessionDataStore.edit { it.clear() }
        }
    }
