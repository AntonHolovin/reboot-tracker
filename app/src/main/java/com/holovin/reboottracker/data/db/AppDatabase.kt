package com.holovin.reboottracker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.holovin.reboottracker.data.db.entity.RebootEventDao
import com.holovin.reboottracker.data.db.model.RebootEventEntity
import com.holovin.reboottracker.data.db.util.InstantConverter

@Database(entities = [RebootEventEntity::class], version = 1, exportSchema = false)
@TypeConverters(InstantConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun rebootEventDao(): RebootEventDao
}