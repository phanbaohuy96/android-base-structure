package com.pbh.androidbase.domain.usecase

import com.pbh.androidbase.domain.entity.Item
import com.pbh.androidbase.domain.model.AppResult
import com.pbh.androidbase.domain.repository.ItemRepository

/** Loads one item for the detail screen through the item repository contract. */
class GetItemDetailUseCase(
    private val itemRepository: ItemRepository,
) {
    /** Returns the item for [id], or a typed failure when unavailable. */
    suspend operator fun invoke(id: String): AppResult<Item> = itemRepository.getItem(id)
}
