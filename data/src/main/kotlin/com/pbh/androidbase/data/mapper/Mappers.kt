package com.pbh.androidbase.data.mapper

import com.pbh.androidbase.data.local.entity.ItemEntity
import com.pbh.androidbase.data.remote.dto.ItemDto
import com.pbh.androidbase.data.remote.dto.SessionDto
import com.pbh.androidbase.domain.entity.Item
import com.pbh.androidbase.domain.entity.UserSession

fun SessionDto.toDomain(): UserSession = UserSession(userId = userId, email = email, token = token)

fun ItemDto.toEntity(): ItemEntity =
    ItemEntity(
        id = id,
        title = title,
        description = description,
        imageUrl = imageUrl,
        updatedAtMillis = updatedAtMillis,
    )

fun ItemEntity.toDomain(): Item =
    Item(
        id = id,
        title = title,
        description = description,
        imageUrl = imageUrl,
        updatedAtMillis = updatedAtMillis,
    )
