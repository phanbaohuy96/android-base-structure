package com.pbh.androidbase.data.remote.api

import com.pbh.androidbase.core.network.ApiResponse
import com.pbh.androidbase.data.remote.dto.ItemDto
import retrofit2.http.GET

/** Retrofit API for item endpoints. */
interface ItemApi {
    /** Fetches the remote item list response envelope. */
    @GET("items")
    suspend fun getItems(): ApiResponse<List<ItemDto>>
}
