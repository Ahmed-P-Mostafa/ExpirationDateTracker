package com.polotika.expirydatetracker.feature_track.app

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.polotika.expirydatetracker.feature_track.domain.usecases.NotificationWorker
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class RebootBroadCastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Intent.ACTION_BOOT_COMPLETED -> {
                startPeriodicWorkManager(context)
            }
        }
    }

    private fun startPeriodicWorkManager(context: Context) {
        val workRequest =
            PeriodicWorkRequestBuilder<NotificationWorker>(6, TimeUnit.HOURS)
                .setInitialDelay(5,TimeUnit.MINUTES).build()
        WorkManager.getInstance(context).enqueue(workRequest)
    }
}