package com.pbh.androidbase.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pbh.androidbase.data.local.dao.ItemDao
import com.pbh.androidbase.data.local.entity.ItemEntity

@Database(
    entities = [ItemEntity::class],
    version = 1,
    exportSchema = true,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao
}
