package com.holovin.reboottracker.data.db.entity

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.holovin.reboottracker.data.db.model.RebootEventEntity

@Dao
interface RebootEventDao {
    @Query(value = "SELECT * FROM reboot_events ORDER BY date ASC")
    suspend fun getEventsAsc(): List<RebootEventEntity>

    @Query(value = "SELECT * FROM reboot_events ORDER BY date DESC")
    suspend fun getEventsDesc(): List<RebootEventEntity>

    @Insert
    suspend fun insertEvent(event: RebootEventEntity)
}