package com.pbh.androidbase.domain.usecase

import com.pbh.androidbase.domain.entity.Item
import com.pbh.androidbase.domain.repository.ItemRepository
import kotlinx.coroutines.flow.Flow

/** Observes the cached item list used by the home screen. */
class GetItemsUseCase(
    private val itemRepository: ItemRepository,
) {
    /** Emits item list updates from the repository. */
    operator fun invoke(): Flow<List<Item>> = itemRepository.observeItems()
}
