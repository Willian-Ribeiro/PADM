package br.edu.ufabc.padm.flickrgallery

import android.app.IntentService
import android.content.Intent
import android.os.IBinder

object FlickrServiceContract {
    // commands
    const val RECENTS_DOWNLOAD_ACTION = "photoList"
    const val THUMBNAIL_DOWNLOAD_ACTION = "thumbnail"
    const val THUMBNAIL_DOWNLOAD_EXTRA = "thumbnailExtra"

    // broadcasts
    const val STATUS_BROADCAST = "statusBroadcast"
    const val STATUS_BROADCAST_EXTRA = "statusBroadcastExtra"

    const val RECENTS_LIST_READY = "recentsListReady"
    const val RECENTS_LIST_READY_EXTRA = "recentsListReadyExtra"

    const val THUMBNAIL_READY = "thumbnailReady"
    const val THUMBNAIL_READY_EXTRA = "thumbnailReadyExtra"
}

class FlickrService : IntentService("FlickrService") {

    override fun onHandleIntent(intent: Intent?) {
        // check network connectivity
        if (!App.isNetworkConnected()) {
            Intent(FlickrServiceContract.STATUS_BROADCAST).apply {
                putExtra(FlickrServiceContract.STATUS_BROADCAST_EXTRA,
                        getString(R.string.no_network))
                App.sendBroadcast(this)
            }
        } else when (intent?.action) {
            FlickrServiceContract.RECENTS_DOWNLOAD_ACTION -> downloadRecents()
            FlickrServiceContract.THUMBNAIL_DOWNLOAD_ACTION -> {
                intent.takeIf {
                    it.hasExtra(FlickrServiceContract.THUMBNAIL_DOWNLOAD_EXTRA)
                }?.apply {
                    downloadThumbnail(intent.getSerializableExtra(
                            FlickrServiceContract.THUMBNAIL_DOWNLOAD_EXTRA) as FlickrPhoto)
                }
            }
            else -> {
                Intent(FlickrServiceContract.STATUS_BROADCAST).apply {
                    putExtra(
                            FlickrServiceContract.STATUS_BROADCAST_EXTRA,
                            getString(R.string.unknown_command)
                    )
                    App.sendBroadcast(this)
                }
            }
        }
    }

    private fun downloadRecents() {
        Intent(FlickrServiceContract.RECENTS_LIST_READY).apply {
            putExtra(FlickrServiceContract.RECENTS_LIST_READY_EXTRA, FlickrFetcher.fetchRecentVideos())
            App.sendBroadcast(this)
        }
    }

    private fun downloadThumbnail(flickrPhoto: FlickrPhoto) {
        val resultPhoto = FlickrThumbnailDownloader.download(flickrPhoto)

        Intent(FlickrServiceContract.THUMBNAIL_READY).apply {
            putExtra(FlickrServiceContract.THUMBNAIL_READY_EXTRA, resultPhoto)
            App.sendBroadcast(this)
        }
    }


    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }
}
