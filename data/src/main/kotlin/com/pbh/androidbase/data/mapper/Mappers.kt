package com.pbh.androidbase.data.mapper

import com.pbh.androidbase.data.local.entity.ItemEntity
import com.pbh.androidbase.data.remote.dto.ItemDto
import com.pbh.androidbase.data.remote.dto.SessionDto
import com.pbh.androidbase.domain.entity.Item
import com.pbh.androidbase.domain.entity.UserSession

/** Converts a network session payload into the domain session model. */
fun SessionDto.toDomain(): UserSession = UserSession(userId = userId, email = email, token = token)

/** Converts a network item payload into the Room cache model. */
fun ItemDto.toEntity(): ItemEntity =
    ItemEntity(
        id = id,
        title = title,
        description = description,
        imageUrl = imageUrl,
        updatedAtMillis = updatedAtMillis,
    )

/** Converts a cached item into the domain item model. */
fun ItemEntity.toDomain(): Item =
    Item(
        id = id,
        title = title,
        description = description,
        imageUrl = imageUrl,
        updatedAtMillis = updatedAtMillis,
    )
