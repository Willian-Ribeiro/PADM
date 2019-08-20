package br.edu.ufabc.padm.flickrgallery

import android.content.Intent
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

/**
 * AsyncTask to download the thumbnail of a single image
 */
object FlickrThumbnailDownloader {
    private val LOGTAG = this::class.java.getSimpleName()

    /**
     * Download a thumbnail
     * @param params the thumbnail url
     * @return the file descriptor of the cached file
     */
    fun download(flickrPhoto: FlickrPhoto): FlickrPhoto {

        val localPhoto = flickrPhoto.copy()

        try {
            val url = URL(flickrPhoto.thumbURL)
            val conn = url.openConnection() as HttpURLConnection

            conn.connect()

            val responseCode = conn.responseCode

            if (responseCode == HttpURLConnection.HTTP_OK) {
                Log.d(LOGTAG, "Downloading thumbnail id: " + flickrPhoto.id)
                val outputFile = File(App.appContext.cacheDir, flickrPhoto.id.toString())
                val outputStream = FileOutputStream(outputFile)
                val bytes = conn.inputStream.copyTo(outputStream)

                Log.d(LOGTAG, "Wrote ${bytes} bytes for thumbnail ${flickrPhoto.id}")

                localPhoto.thumbFile = outputFile
                outputStream.close()
            } else {
                sendStatus(R.string.thumbnail_download_error)
                Log.e(LOGTAG, "Failed to download thumbnail. HTTP code: $responseCode")
            }
            conn.disconnect()
        } catch (e: MalformedURLException) {
            sendStatus(R.string.thumbnail_download_error)
            Log.e(LOGTAG, "Malformed thumbnail URL", e)
        } catch (e: IOException) {
            sendStatus(R.string.thumbnail_download_error)
            Log.e(LOGTAG, "Failed to open thumbnail URL", e)
        }

        return localPhoto
    }

    private fun sendStatus(messageId : Int) {
        Intent(FlickrServiceContract.STATUS_BROADCAST).apply {
            putExtra(FlickrServiceContract.STATUS_BROADCAST_EXTRA,
                    App.appContext.getString(messageId))
            App.sendBroadcast(this)
        }
    }

}