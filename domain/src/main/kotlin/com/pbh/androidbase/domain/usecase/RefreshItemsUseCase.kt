package com.pbh.androidbase.domain.usecase

import com.pbh.androidbase.domain.model.AppResult
import com.pbh.androidbase.domain.repository.ItemRepository

class RefreshItemsUseCase(
    private val itemRepository: ItemRepository,
) {
    suspend operator fun invoke(): AppResult<Unit> = itemRepository.refreshItems()
}
