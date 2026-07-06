package com.pbh.androidbase.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pbh.androidbase.data.local.dao.ItemDao
import com.pbh.androidbase.data.local.entity.ItemEntity

/** Room database for locally cached app data. */
@Database(
    entities = [ItemEntity::class],
    version = 1,
    exportSchema = true,
)
abstract class AppDatabase : RoomDatabase() {
    /** DAO for offline-first item reads and writes. */
    abstract fun itemDao(): ItemDao
}
