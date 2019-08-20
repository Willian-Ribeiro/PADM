package br.edu.ufabc.padm.flickrgallery

import android.content.Intent
import android.net.Uri
import android.util.Log
import org.json.JSONException
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

/**
 * Utility class to interact with the Flickr web service
 *
 * Currently, it only works with the flickr.flickrPhotoList.getRecent method
 */
object FlickrFetcher {

    /**
     * A contract for the web service API
     */
    private object FlickrContract {
        val FLICKR_REST_ENDPOINT = "http://10.0.2.2:8099/videos"
//        val API_KEY_K = "api_key"
//        val API_KEY_V = "ad09fc474fe44fd5418951b62b121cbd" // change to your own key
//        val FORMAT_K = "format"
//        val FORMAT_V = "json"
//        val EXTRAS_K = "extras"
//        val EXTRAS_V = "url_s"
//        val METHOD_K = "method"
//        val GET_RECENT_METHOD = "flickr.photos.getRecent"
//        val NO_JSON_CALLBACK_K = "nojsoncallback"
//        val NO_JSON_CALLBACK_V = "1"
    }

    private val BUFFER_SIZE = 1024
    private val LOGTAG = FlickrFetcher::class.java.getSimpleName()

    /**
     * Fetch a list of recent flickrPhotoList from Flickr
     * @return
     */
    fun fetchRecentVideos(): FlickrPhotoList {
        val photos = FlickrPhotoList()
        val url: URL
        var urlStr: String? = null
        var conn: HttpURLConnection? = null
        val responseCode: Int
        val responseContent: String
        val flickrJSONSerializer: FlickrJSONSerializer

        try {
            /*urlStr = Uri.parse(FlickrContract.FLICKR_REST_ENDPOINT).buildUpon()
                    .appendQueryParameter(FlickrContract.METHOD_K, FlickrContract.GET_RECENT_METHOD)
                    .appendQueryParameter(FlickrContract.API_KEY_K, FlickrContract.API_KEY_V)
                    .appendQueryParameter(FlickrContract.FORMAT_K, FlickrContract.FORMAT_V)
                    .appendQueryParameter(FlickrContract.EXTRAS_K, FlickrContract.EXTRAS_V)
                    .appendQueryParameter(FlickrContract.NO_JSON_CALLBACK_K, FlickrContract.NO_JSON_CALLBACK_V)
                    .build().toString()*/
            urlStr = Uri.parse(FlickrContract.FLICKR_REST_ENDPOINT).toString()

            Log.d(LOGTAG, urlStr)
            url = URL(urlStr)
            conn = url.openConnection() as HttpURLConnection
            conn.connect()
            responseCode = conn.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val outputStream = ByteArrayOutputStream()
                val inputStream = conn.inputStream
                val buffer = ByteArray(BUFFER_SIZE)

                var len = inputStream.read(buffer)
                while (len > 0) {
                    outputStream.write(buffer, 0, len)
                    len = inputStream.read(buffer)
                }
                outputStream.flush()
                outputStream.close()
                inputStream.close()
                responseContent = String(outputStream.toByteArray())
                try {
                    flickrJSONSerializer = FlickrJSONSerializer(responseContent)
                    if (flickrJSONSerializer.isStatusOk)
                        photos.addAll(flickrJSONSerializer.deserializeRecentVideos())
                } catch (e: JSONException) {
                    Log.e(LOGTAG, "Failed to parse JSON response document", e)
                    sendStatus(R.string.parsing_failed)
                }

                Log.d(LOGTAG, "Total flickrPhotoList retrieved: " + photos.size())
            } else {
                Log.e(LOGTAG, "Wrong response code while getting Flickr doc: $responseCode")
                sendStatus(R.string.connection_failed)
            }
        } catch (e: MalformedURLException) {
            Log.e(LOGTAG, "Malformed URL: $urlStr", e)
            sendStatus(R.string.connection_failed)
        } catch (e: IOException) {
            Log.e(LOGTAG, "Failed to connect to URL", e)
            sendStatus(R.string.connection_failed)
        } finally {
            conn?.disconnect()
        }

        return photos
    }

    private fun sendStatus(messageId : Int) {
        Intent(FlickrServiceContract.STATUS_BROADCAST).apply {
            putExtra(FlickrServiceContract.STATUS_BROADCAST_EXTRA,
                    App.appContext.getString(messageId))
            App.sendBroadcast(this)
        }
    }

}