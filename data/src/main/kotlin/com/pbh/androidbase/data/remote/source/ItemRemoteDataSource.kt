package com.pbh.androidbase.data.remote.source

import com.pbh.androidbase.data.remote.api.ItemApi
import com.pbh.androidbase.data.remote.dto.ItemDto
import javax.inject.Inject

class ItemRemoteDataSource
    @Inject
    constructor(
        private val itemApi: ItemApi,
    ) {
        suspend fun getItems(): List<ItemDto> = itemApi.getItems().data
    }
