package com.pbh.androidbase.data.remote.source

import com.pbh.androidbase.data.remote.api.ItemApi
import com.pbh.androidbase.data.remote.dto.ItemDto
import javax.inject.Inject

/** Remote source that unwraps item API responses for repositories. */
class ItemRemoteDataSource
    @Inject
    constructor(
        private val itemApi: ItemApi,
    ) {
        /** Fetches remote items from the configured API. */
        suspend fun getItems(): List<ItemDto> = itemApi.getItems().data
    }
