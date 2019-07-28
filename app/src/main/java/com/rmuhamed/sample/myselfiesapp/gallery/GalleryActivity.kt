package com.rmuhamed.sample.myselfiesapp.gallery

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.rmuhamed.sample.myselfiesapp.R
import com.rmuhamed.sample.myselfiesapp.camera.CameraActivity
import kotlinx.android.synthetic.main.activity_gallery.*

class GalleryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        val viewModel = ViewModelProviders.of(this).get(GalleryViewModel::class.java)

        viewModel.albumPhotosLiveData.observe(this, Observer {
            it?.let {
                gallery_pictures.visibility = View.VISIBLE
                gallery_pictures.adapter = GalleryAdapter(images = it)
            } ?: run {
                gallery_empty_image.visibility = View.VISIBLE
                gallery_empty_label.visibility = View.VISIBLE
            }
        })

        gallery_take_new_picture_button.setOnClickListener {
            //TODO: RM - Check existence first
            viewModel.createAlbum()
            progress.visibility = View.VISIBLE
        }

        viewModel.albumNotCreatedLiveData.observe(this, Observer {
            it?.let {
                Snackbar.make(gallery_pictures, it, BaseTransientBottomBar.LENGTH_LONG).show()
                progress.visibility = View.GONE
            }
        })

        viewModel.albumCreationLiveData.observe(this, Observer {
            it.data?.let {
                this@GalleryActivity.startActivity(
                    Intent(
                        this@GalleryActivity,
                        CameraActivity::class.java
                    )
                )
            }
        })
    }
}