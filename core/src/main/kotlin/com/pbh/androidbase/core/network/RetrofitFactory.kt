package com.pbh.androidbase.core.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

/** Factory for Retrofit instances that share the app's Kotlin serialization setup. */
object RetrofitFactory {
    /** Creates a Retrofit client for [baseUrl] using [client] and [json]. */
    fun create(
        baseUrl: String,
        client: OkHttpClient,
        json: Json,
    ): Retrofit =
        Retrofit
            .Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
}
