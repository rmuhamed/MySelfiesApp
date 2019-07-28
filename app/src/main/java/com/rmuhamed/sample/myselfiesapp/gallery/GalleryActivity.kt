package com.rmuhamed.sample.myselfiesapp.gallery

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
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
            progress.visibility = View.GONE
            it?.let {
                if (it.isEmpty()) {
                    gallery_empty_image.visibility = View.VISIBLE
                    gallery_empty_label.visibility = View.VISIBLE
                } else {
                    gallery_pictures.visibility = View.VISIBLE
                    gallery_pictures.adapter = GalleryAdapter(images = it)
                }
            } ?: run {
                Toast.makeText(
                    this,
                    R.string.gallery_pictures_could_not_retrieved,
                    Toast.LENGTH_LONG
                ).show()
            }
        })

        gallery_take_new_picture_button.setOnClickListener {
            if (!viewModel.existsAnAlbum()) {
                viewModel.createAlbum()
            } else {
                this@GalleryActivity.takeNewPicture(albumId = viewModel.albumId)
            }
        }

        viewModel.albumNotCreatedLiveData.observe(this, Observer {
            it?.let {
                Snackbar.make(gallery_pictures, it, BaseTransientBottomBar.LENGTH_LONG).show()
                progress.visibility = View.GONE
            }
        })

        viewModel.albumCreationLiveData.observe(this, Observer {
            it?.let {
                this@GalleryActivity.takeNewPicture(albumId = it)
            }
        })
    }

    private fun takeNewPicture(albumId: String) {
        this@GalleryActivity.startActivity(
            Intent().apply {
                setClass(this@GalleryActivity, CameraActivity::class.java)
                putExtra("ALBUM_ID", albumId)
            }
        )
    }
}