package com.holovin.reboottracker.data.repository

import com.holovin.reboottracker.data.db.AppDatabase
import com.holovin.reboottracker.data.db.model.RebootEventEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RebootEventRepository @Inject constructor(
    private val database: AppDatabase
) {
    suspend fun getEvents(sortedDesc: Boolean): List<RebootEventEntity> {
        return withContext(Dispatchers.IO) {
            if (sortedDesc) {
                database.rebootEventDao().getEventsDesc()
            } else {
                database.rebootEventDao().getEventsAsc()
            }
        }
    }

    suspend fun saveEvent() {
        withContext(Dispatchers.IO) {
            database.rebootEventDao()
                .insertEvent(RebootEventEntity(date = Instant.now()))
        }
    }
}