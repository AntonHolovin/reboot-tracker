package com.holovin.reboottracker.tracking

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.holovin.reboottracker.data.repository.RebootEventRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BootCompletedReceiver : BroadcastReceiver() {
    @Inject
    lateinit var repository: RebootEventRepository

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            GlobalScope.launch { repository.saveEvent() }
        }
    }
}