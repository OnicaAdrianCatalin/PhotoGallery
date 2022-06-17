package com.example.photogallery

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Handler
import android.os.HandlerThread
import android.os.Message
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.photogallery.data.repository.PhotoGalleryRepository
import java.util.concurrent.ConcurrentHashMap

class ThumbnailDownloader<in T : Any>(
    private val responseHandler: Handler,
    private val onThumbnailDownloaded: (T, Bitmap) -> Unit

) : HandlerThread(TAG) {
    private lateinit var requestHandler: Handler
    private val requestMap = ConcurrentHashMap<T, String>()
    private val flickrRepository = PhotoGalleryRepository.get()

    private var hasQuit = false

    val fragmentLifecycleObserver = LifecycleEventObserver { _, event ->
        if (event == Lifecycle.Event.ON_CREATE) {
            Log.i(TAG, "Starting background thread")
            start()
            looper
        } else if (event == Lifecycle.Event.ON_DESTROY) {
            Log.i(TAG, "Destroying background thread")
            quit()
        }
    }

    val viewLifecycleObserver = LifecycleEventObserver { _, event ->
        if (event == Lifecycle.Event.ON_DESTROY) {
            Log.i(TAG, "Clearing all requests from queue")
            requestHandler.removeMessages(MESSAGE_DOWNLOAD)
            requestMap.clear()
        }
    }

    override fun quit(): Boolean {
        hasQuit = true
        return super.quit()
    }

    @Suppress("UNCHECKED_CAST")
    @SuppressLint("HandlerLeak")
    override fun onLooperPrepared() {
        requestHandler = object : Handler(looper) {
            override fun handleMessage(msg: Message) {
                if (msg.what == MESSAGE_DOWNLOAD) {
                    val target = msg.obj as T
                    Log.i(TAG, "Got a request for URL: ${requestMap[target]}")
                    handleRequest(target)
                }
            }
        }
    }

    fun handleRequest(target: T) {
        val url = requestMap[target] ?: return
        val bitmap = flickrRepository.fetchPhoto(url) ?: return
        responseHandler.post(
            Runnable {
                if (requestMap[target] != url || hasQuit) {
                    return@Runnable
                }
                requestMap.remove(target)
                onThumbnailDownloaded(target, bitmap)
            }
        )
    }

    fun queueThumbnail(target: T, url: String) {
        Log.i(TAG, "Got a URL: $url ")
        requestMap[target] = url
        requestHandler.obtainMessage(MESSAGE_DOWNLOAD, target).sendToTarget()
    }

    companion object {
        private const val TAG = "ThumbnailDownloader"
        private const val MESSAGE_DOWNLOAD = 0
    }
}
