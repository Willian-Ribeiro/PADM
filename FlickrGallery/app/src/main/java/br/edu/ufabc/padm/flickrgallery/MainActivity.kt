package br.edu.ufabc.padm.flickrgallery

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {

    private lateinit var recyclerGridView: RecyclerView

    private val photoListReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.takeIf { it.hasExtra(FlickrServiceContract.RECENTS_LIST_READY_EXTRA) }?.apply {
                FlickrPhotoAdapter(this@MainActivity).apply {
                    flickrPhotoList = intent.getSerializableExtra(
                            FlickrServiceContract.RECENTS_LIST_READY_EXTRA) as FlickrPhotoList
                    recyclerGridView.adapter = this
                    (recyclerGridView.adapter as FlickrPhotoAdapter).notifyDataSetChanged()
                }
            }
        }
    }

    private val flickrServiceStatus = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.takeIf { it.hasExtra(FlickrServiceContract.STATUS_BROADCAST_EXTRA) }?.apply {
                Snackbar.make(
                        findViewById(R.id.layout_main),
                        getStringExtra(FlickrServiceContract.STATUS_BROADCAST_EXTRA),
                        Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery_flickr)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        title = getString(R.string.alternate_main_title)


        recyclerGridView = findViewById(R.id.photo_grid)
        recyclerGridView.setHasFixedSize(true)
        recyclerGridView.layoutManager = GridLayoutManager(this, 1)


    }

    override fun onStart() {
        super.onStart()
        App.registerBroadcast(photoListReceiver, IntentFilter(FlickrServiceContract.RECENTS_LIST_READY))
        App.registerBroadcast(flickrServiceStatus, IntentFilter(FlickrServiceContract.STATUS_BROADCAST))

        Intent(this, FlickrService::class.java).apply {
            action = FlickrServiceContract.RECENTS_DOWNLOAD_ACTION
            startService(this)
        }
    }

    override fun onStop() {
        super.onStop()

        App.unregisterBroadcast(photoListReceiver)
        App.unregisterBroadcast(flickrServiceStatus)
    }

}
