package com.rmuhamed.sample.myselfiesapp.gallery

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.rmuhamed.sample.myselfiesapp.ACCESS_TOKEN
import com.rmuhamed.sample.myselfiesapp.R
import com.rmuhamed.sample.myselfiesapp.USER_NAME
import com.rmuhamed.sample.myselfiesapp.camera.CameraActivity
import com.rmuhamed.sample.myselfiesapp.getViewModel
import com.rmuhamed.sample.myselfiesapp.repository.GalleryRepository
import kotlinx.android.synthetic.main.activity_gallery.*

class GalleryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        val accessToken = intent.getStringExtra(ACCESS_TOKEN)
        val userName = intent.getStringExtra(USER_NAME)

        val viewModel = getViewModel { GalleryViewModel(GalleryRepository(accessToken, userName)) }

        viewModel.photosRetrievedLiveData.observe(this, Observer {
            progress.visibility = View.GONE
            it?.let {
                if (it.isEmpty()) {
                    gallery_empty_image.visibility = View.VISIBLE
                    gallery_empty_label.visibility = View.VISIBLE
                } else {
                    gallery_pictures.visibility = View.VISIBLE
                    gallery_pictures.adapter = GalleryAdapter(images = it)
                }
            }
        })

        viewModel.errorLiveData.observe(this, Observer {
            it?.let {
                Snackbar.make(
                    gallery_pictures,
                    it,
                    BaseTransientBottomBar.LENGTH_SHORT
                ).show()
            }
        })

        viewModel.photosNotRetrievedLiveData.observe(this, Observer {
            if (it) {
                Snackbar.make(
                    gallery_pictures,
                    R.string.gallery_pictures_could_not_retrieved,
                    BaseTransientBottomBar.LENGTH_SHORT
                ).show()
            }
        })

        gallery_take_new_picture_button.setOnClickListener {
            if (!viewModel.existsAnAlbum()) {
                //TODO: RM - Show Dialog with two fields
            } else {
                this@GalleryActivity.takeNewPicture(
                    albumId = viewModel.albumId,
                    accessToken = accessToken!!
                )
            }
        }

        viewModel.albumNotCreatedLiveData.observe(this, Observer {
            it?.let {
                Snackbar.make(gallery_pictures, it, BaseTransientBottomBar.LENGTH_SHORT).show()
                progress.visibility = View.GONE
            }
        })

        viewModel.albumCreationLiveData.observe(this, Observer {
            it?.let {
                this@GalleryActivity.takeNewPicture(albumId = it, accessToken = accessToken!!)
            }
        })
    }

    private fun takeNewPicture(albumId: String, accessToken: String) {
        this@GalleryActivity.startActivity(
            Intent().apply {
                setClass(this@GalleryActivity, CameraActivity::class.java)
                putExtra("ALBUM_ID", albumId)
                putExtra("ACCESS_TOKEN", accessToken)
            }
        )
    }
}