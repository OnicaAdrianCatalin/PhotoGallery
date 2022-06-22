package com.example.photogallery.data

import android.app.PendingIntent
import android.content.Context
import android.content.res.Resources
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.photogallery.PhotoGalleryApplication
import com.example.photogallery.R
import com.example.photogallery.data.model.GalleryItem
import com.example.photogallery.data.repository.PhotoGalleryRepository
import com.example.photogallery.presentation.PhotoGalleryActivity

class PollWorker(private val context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {
    private val repository = PhotoGalleryRepository.get()

    override fun doWork(): Result {
        val items: List<GalleryItem> = getListOfPhotos()
        if (items.isEmpty()) {
            return Result.success()
        }
        val lastResultId = QueryPreferences.getLastResultId(context)
        val resultId = items.first().id
        if (resultId == lastResultId) {
            Log.i(TAG, "got an old result: $resultId")
        } else {
            Log.i(TAG, "got a new result: $resultId")
            QueryPreferences.setLastResultId(context, resultId)
            val intent = PhotoGalleryActivity.newIntent(context)
            val pendingIntent = PendingIntent.getActivity(
                context, 0, intent, PendingIntent.FLAG_IMMUTABLE
            )
            onCreateNotification(context.resources, pendingIntent)
        }
        return Result.success()
    }

    private fun getListOfPhotos(): List<GalleryItem> {
        val query = QueryPreferences.getStoredQuery(context)
        return if (query.isNullOrEmpty()) {
            repository.fetchPhotosRequest().execute().body()?.photos?.galleryItems
        } else {
            repository.searchPhotosRequest(query).execute().body()?.photos?.galleryItems
        } ?: emptyList()
    }

    private fun onCreateNotification(
        resources: Resources,
        pendingIntent: PendingIntent?
    ) {
        val notification = NotificationCompat
            .Builder(context, PhotoGalleryApplication.NOTIFICATION_CHANNEL_ID)
            .setTicker(resources.getString(R.string.new_pictures_title))
            .setSmallIcon(android.R.drawable.ic_menu_report_image)
            .setContentTitle(resources.getString(R.string.new_pictures_title))
            .setContentText(resources.getString(R.string.new_pictures_text))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(0, notification)
    }

    companion object {
        private const val TAG = "PollWorker"
    }
}
