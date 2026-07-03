package com.pbh.androidbase.domain.usecase

import com.pbh.androidbase.domain.entity.Item
import com.pbh.androidbase.domain.repository.ItemRepository
import kotlinx.coroutines.flow.Flow

class GetItemsUseCase(
    private val itemRepository: ItemRepository,
) {
    operator fun invoke(): Flow<List<Item>> = itemRepository.observeItems()
}
