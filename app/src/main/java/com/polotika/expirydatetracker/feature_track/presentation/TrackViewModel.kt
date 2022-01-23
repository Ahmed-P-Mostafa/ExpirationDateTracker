package com.polotika.expirydatetracker.feature_track.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.await
import com.polotika.expirydatetracker.feature_track.domain.repository.PreferencesRepository
import com.polotika.expirydatetracker.feature_track.domain.usecases.NotificationWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class TrackViewModel @Inject constructor(private val prefs: PreferencesRepository) : ViewModel() {

    fun init(context: Context) {
        isAppFirstTimeRun(context)
    }

    private suspend fun startPeriodicWorkManager(context: Context) {
        val workRequest =
            PeriodicWorkRequestBuilder<NotificationWorker>(6, TimeUnit.HOURS)
                .setInitialDelay(5, TimeUnit.MINUTES).build()
        WorkManager.getInstance(context).enqueue(workRequest).await()
    }


    private fun isAppFirstTimeRun(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            prefs.getFirstTime().collect {
                if (it) {
                    startPeriodicWorkManager(context)
                    prefs.setFirstTime(false)
                }
            }
        }
    }
}