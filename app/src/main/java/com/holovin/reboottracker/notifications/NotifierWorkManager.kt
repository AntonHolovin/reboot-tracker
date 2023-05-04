package com.holovin.reboottracker.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.holovin.reboottracker.R
import com.holovin.reboottracker.data.db.model.RebootEventEntity
import com.holovin.reboottracker.data.repository.RebootEventRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.concurrent.TimeUnit

private const val NOTIFICATION_WORK_NAME = "notification_work"
private const val NOTIFICATION_CHANNEL_ID = "reboot_tracker_channel"
private const val NOTIFICATION_ID = 1

object NotifierWorkManager {
    fun start(context: Context) {
        val request = PeriodicWorkRequestBuilder<NotifierWorker>(15, TimeUnit.MINUTES)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            NOTIFICATION_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            request,
        )
    }
}

@HiltWorker
class NotifierWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted params: WorkerParameters,
    private val rebootEventRepository: RebootEventRepository
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        // this might be optimized by using a special query with "LIMIT 2" part
        val events = rebootEventRepository.getEvents(true).take(2)

        val body = buildString(events)

        showNotification(body)

        return Result.success()
    }

    private fun showNotification(body: String) {
        val notificationManager = context.getSystemService<NotificationManager>()!!

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Reboot Tracker"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance)
            notificationManager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.male)
            .setContentTitle("Reboot Tracker")
            .setContentText(body)
            .setAutoCancel(false)

        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    private fun buildString(events: List<RebootEventEntity>): String {
        return when {
            events.isEmpty() -> "No boots detected"

            events.size == 1 -> "The boot was detected with the timestamp = ${events.first().date.toEpochMilli()}"

            events.size == 2 -> {
                val diff = events[0].date.toEpochMilli() - events[1].date.toEpochMilli()
                "Last boots time delta = $diff"
            }

            else -> ""
        }
    }
}