package com.pbh.androidbase.core.network

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse<T>(
    val data: T,
    val message: String? = null,
)
