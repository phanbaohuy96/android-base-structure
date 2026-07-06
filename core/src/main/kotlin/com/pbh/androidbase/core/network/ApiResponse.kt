package com.pbh.androidbase.core.network

import kotlinx.serialization.Serializable

/** Generic API envelope expected from Retrofit endpoints in this template. */
@Serializable
data class ApiResponse<T>(
    val data: T,
    val message: String? = null,
)
