package br.edu.ufabc.padm.flickrgallery

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * Adapter for the RecyclerView
 */
class FlickrPhotoAdapter(private val mainActivity: MainActivity) : RecyclerView.Adapter<FlickrPhotoAdapter.FlickrPhotoViewHolder>() {
    var flickrPhotoList = FlickrPhotoList()

    private val LOGTAG = this::class.java.name

    inner class FlickrPhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView = itemView.findViewById(R.id.flickr_thumbnail)
        var titleView: TextView = itemView.findViewById(R.id.flickr_title)
        var dateView: TextView = itemView.findViewById(R.id.flickr_date)
        var viewsView: TextView = itemView.findViewById(R.id.flickr_views)
        lateinit var photo: FlickrPhoto

        val thumbnailReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val self = this
                intent?.takeIf { it.hasExtra(FlickrServiceContract.THUMBNAIL_READY_EXTRA) }?.apply {
                    val receivedPhoto =
                            intent.getSerializableExtra(FlickrServiceContract.THUMBNAIL_READY_EXTRA) as FlickrPhoto
                    if (receivedPhoto.hasThumbFile() && receivedPhoto.id == photo.id) {
                        // XXX: decoding the image from disk in the UI thread can make it unresponsive
                        // TODO: decode the image in a background thread
                        imageView.setImageBitmap(
                                BitmapFactory.decodeStream(receivedPhoto.thumbFile.inputStream()))
                        titleView.text = receivedPhoto.title
                        dateView.text = receivedPhoto.recorded
                        viewsView.text = receivedPhoto.views.toString()

                        App.unregisterBroadcast(self)
                    }
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlickrPhotoViewHolder {
        return FlickrPhotoViewHolder(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.gallery_item, parent, false))
    }

    override fun onBindViewHolder(holder: FlickrPhotoViewHolder, position: Int) {
        holder.imageView.setImageBitmap(null) // make it blank
        holder.photo = flickrPhotoList.getPhotoAt(position)
//        holder.imageView.setOnClickListener { startActivity(Intent(parent.context, )) }
        App.registerBroadcast(holder.thumbnailReceiver,
                IntentFilter(FlickrServiceContract.THUMBNAIL_READY))
        fetchThumbnail(holder.photo)
    }

    override fun getItemCount(): Int {
        return flickrPhotoList.size()
    }

    /**
     * Send a command to FlickrService to download a photo thumbnail
     * Response is received via broadcast
     */
    private fun fetchThumbnail(flickrPhoto: FlickrPhoto) {
        Intent(mainActivity, FlickrService::class.java).apply {
            action = FlickrServiceContract.THUMBNAIL_DOWNLOAD_ACTION
            putExtra(FlickrServiceContract.THUMBNAIL_DOWNLOAD_EXTRA, flickrPhoto)
            mainActivity.startService(this)
        }
    }

}