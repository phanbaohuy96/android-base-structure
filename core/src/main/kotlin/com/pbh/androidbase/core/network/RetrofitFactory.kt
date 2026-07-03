package com.pbh.androidbase.core.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

object RetrofitFactory {
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
