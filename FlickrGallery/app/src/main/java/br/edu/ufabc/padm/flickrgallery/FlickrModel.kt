package br.edu.ufabc.padm.flickrgallery

import java.io.File
import java.io.Serializable

/**
 * A serializable wrapper to simplify casts in receivers
 */
class FlickrPhotoList : Serializable {
    val photos : ArrayList<FlickrPhoto> = ArrayList()

    fun add(photo: FlickrPhoto) = photos.add(photo)

    fun remove(photo: FlickrPhoto) = photos.remove(photo)

    fun getPhotoAt(i: Int) = photos[i]

    fun addAll(photoList: FlickrPhotoList) = photos.addAll(photoList.photos)

    fun size() = photos.size
}

// na verdade eh o video
data class FlickrPhoto (
        var id: Long = 0L,
        var title: String = "",
        var event: String = "",
        var duration: String = "",
        var recorded: String = "",
        var speaker: String = "",
        var views: Int = 0,
        var likes: Int = 0,
        var dislikes: Int = 0,
        var source: String = "",
        var videoURL: String = "",
        var thumbURL: String = "",
        var thumbFile: File = File("")) : Serializable {
    fun hasThumbURL(): Boolean  = thumbURL.trim().isNotEmpty()
    fun hasThumbFile(): Boolean = thumbFile.name.trim().isNotEmpty()
}
