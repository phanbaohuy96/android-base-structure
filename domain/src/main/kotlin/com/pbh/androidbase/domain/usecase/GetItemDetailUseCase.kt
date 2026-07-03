package com.pbh.androidbase.domain.usecase

import com.pbh.androidbase.domain.entity.Item
import com.pbh.androidbase.domain.model.AppResult
import com.pbh.androidbase.domain.repository.ItemRepository

class GetItemDetailUseCase(
    private val itemRepository: ItemRepository,
) {
    suspend operator fun invoke(id: String): AppResult<Item> = itemRepository.getItem(id)
}
