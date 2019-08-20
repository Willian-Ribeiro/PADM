package br.edu.ufabc.padm.flickrgallery

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

/**
 * A JSON serializer for the flickr web service. The serializer is stateful, therefore
 * it should be used for a single JSON document
 */
class FlickrJSONSerializer
/**
 * Builds the initializer with a json string
 * @param jsonStr
 * @throws JSONException
 */
@Throws(JSONException::class)
constructor(jsonStr: String) {
    private val root: JSONObject?

    /**
     * Check if status of the action response
     * @return
     * @throws JSONException
     */
    val isStatusOk: Boolean
        @Throws(JSONException::class)
        get() {
            val statusStr = root!!.getString(FlickrJSONContract.VIDEO_ARRAY)
            var isOk = false

            if (statusStr != null) // nao ser nulo ja me eh o suficiente
                isOk = true

            return isOk
        }

    /**
     * A contract for the Flickr JSON format
     */
    private object FlickrJSONContract {
        const val VIDEO_OBJECT = ""
        const val VIDEO_ARRAY = "videos"
        const val VIDEO_ID = "id"
        const val VIDEO_THUMB_URL = "url_s"
        const val VIDEO_TITLE = "title"
        const val VIDEO_EVENT = "event"
        const val VIDEO_DURATION = "duration"
        const val VIDEO_RECORDED = "recorded"
        const val VIDEO_SPEAKER = "speaker"
        const val VIDEO_VIEWS = "views"
        const val VIDEO_LIKES = "likes"
        const val VIDEO_DISLIKES = "dislikes"
        const val VIDEO_SOURCE = "source"
        const val STATUS = "stat"
        const val STATUS_FAILED = "failed"
        const val STATUS_OK = "ok"
    }

    init {
        root = JSONObject(jsonStr)
    }

    /**
     * Deserialize a list of recent flickrPhotoList
     * @return a list of FlickrPhoto objects
     * @throws JSONException
     */
    @Throws(JSONException::class)
    fun deserializeRecentVideos(): FlickrPhotoList {
        val videos = FlickrPhotoList()

        val videosArray: JSONArray

        if (root != null) {
            //videosArray = root.getJSONObject(FlickrJSONContract.VIDEO_OBJECT)
              //      .getJSONArray(FlickrJSONContract.VIDEO_ARRAY)
            videosArray = root.getJSONArray(FlickrJSONContract.VIDEO_ARRAY)
            for (i in 0 until videosArray.length()) {
                val photoObj = videosArray.get(i) as JSONObject
                val video = FlickrPhoto()

                video.id = java.lang.Long.parseLong(photoObj.getString(FlickrJSONContract.VIDEO_ID))
                video.event = photoObj.getString(FlickrJSONContract.VIDEO_EVENT)
                video.title = photoObj.getString(FlickrJSONContract.VIDEO_TITLE)
                video.duration = photoObj.getString(FlickrJSONContract.VIDEO_DURATION)
                video.recorded = photoObj.getString(FlickrJSONContract.VIDEO_RECORDED)
                video.speaker = photoObj.getString(FlickrJSONContract.VIDEO_SPEAKER)
                video.views = Integer.parseInt(photoObj.getString(FlickrJSONContract.VIDEO_VIEWS))
                video.likes = Integer.parseInt(photoObj.getString(FlickrJSONContract.VIDEO_LIKES))
                video.dislikes = Integer.parseInt(photoObj.getString(FlickrJSONContract.VIDEO_DISLIKES))
                video.source = photoObj.getString(FlickrJSONContract.VIDEO_SOURCE)
//                if (photoObj.has(FlickrJSONContract.VIDEO_THUMB_URL))
                video.thumbURL = "http://10.0.2.2:8099/thumbnail/" +
                        java.lang.Long.parseLong(photoObj.getString(FlickrJSONContract.VIDEO_ID))
                video.videoURL = "http://10.0.2.2:8099/video/" +
                        java.lang.Long.parseLong(photoObj.getString(FlickrJSONContract.VIDEO_ID))
                videos.add(video)
            }
        }

        return videos
    }
}
