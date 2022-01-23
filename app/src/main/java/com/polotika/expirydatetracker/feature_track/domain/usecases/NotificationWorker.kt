package com.polotika.expirydatetracker.feature_track.domain.usecases

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.room.Room
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.polotika.expirydatetracker.R
import com.polotika.expirydatetracker.feature_scan.data.local.Mapper
import com.polotika.expirydatetracker.feature_scan.data.local.ProductDatabase
import com.polotika.expirydatetracker.feature_scan.domain.model.Product
import com.polotika.expirydatetracker.ui.HomeActivity
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.random.Random
import kotlin.random.nextInt

@HiltWorker
class NotificationWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParameters: WorkerParameters,
) : Worker(context, workerParameters) {

    override fun doWork(): Result {
        CoroutineScope(Dispatchers.IO).launch {
            for (product: Product in getExpiredItems()) {
                showNotification(product)
            }
        }
        return Result.success()
    }

    private suspend fun getExpiredItems(): List<Product> {
        val db = Room.databaseBuilder(context, ProductDatabase::class.java, "products_db").build()
        val dao = db.dao
        val time = Calendar.getInstance().timeInMillis
        val list =
            dao.getExpiredProducts(time).filter { !it.isNotified }.map { Mapper.toProduct(it) }
        return if (list.isNotEmpty()) list else emptyList()
    }

    private fun showNotification(product: Product) {
        val manager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val intent = Intent(context, HomeActivity::class.java)
        val contentPendingIntent = PendingIntent.getActivity(context, 1001, intent, 0)

        val notification: Notification = NotificationCompat.Builder(context, "CHANNEL_ID")
            .setSmallIcon(R.drawable.ic_scan_24)
            .setContentTitle("${product.name} expired")
            .setContentText("Please caution the ${product.name} item is expired now")
            .setContentIntent(contentPendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .build()

        val id = Random.nextInt(IntRange(1, 10000))
        manager.notify(id, notification)
    }
}