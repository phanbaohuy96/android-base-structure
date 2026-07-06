package com.pbh.androidbase.domain.usecase

import com.pbh.androidbase.domain.model.AppResult
import com.pbh.androidbase.domain.repository.ItemRepository

/** Refreshes the offline-first item cache. */
class RefreshItemsUseCase(
    private val itemRepository: ItemRepository,
) {
    /** Returns success when the local cache was updated, otherwise a typed failure. */
    suspend operator fun invoke(): AppResult<Unit> = itemRepository.refreshItems()
}
