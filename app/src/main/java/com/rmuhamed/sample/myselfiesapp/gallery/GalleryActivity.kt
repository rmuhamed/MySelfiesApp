package com.rmuhamed.sample.myselfiesapp.gallery

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.rmuhamed.sample.myselfiesapp.*
import com.rmuhamed.sample.myselfiesapp.album.CreateAlbumFragment
import com.rmuhamed.sample.myselfiesapp.camera.CameraActivity
import com.rmuhamed.sample.myselfiesapp.repository.RepositoryFactory
import kotlinx.android.synthetic.main.activity_gallery.*

class GalleryActivity : AppCompatActivity() {
    private lateinit var viewModel: GalleryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        viewModel = getViewModel(GalleryViewModel::class.java, application, RepositoryFactory.Type.GALLERY)

        viewModel.loadingLiveData.observe(this, Observer {
            it?.let {
                progress.visibility = if (it) View.VISIBLE else View.GONE
            }
        })

        viewModel.photosRetrievedLiveData.observe(this, Observer {
            it?.let {
                if (it.isEmpty()) {
                    gallery_empty_image.visibility = View.VISIBLE
                    gallery_empty_label.visibility = View.VISIBLE
                    gallery_take_new_picture_button.setText(R.string.gallery_first_picture_label)
                } else {
                    gallery_pictures.adapter = GalleryAdapter(images = it)
                    gallery_take_new_picture_button.setText(R.string.gallery_another_picture_label)
                }

                gallery_take_new_picture_button.visibility = View.VISIBLE
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
                showBottomSheetDialog { CreateAlbumFragment.newInstance("accessToken") }
            } else {
                this@GalleryActivity.takeNewPicture(
                    albumId = viewModel.albumId!!,
                    accessToken = "accessToken!!" //TODO: RM - Change this later
                )
            }
        }
    }

    override fun onRestart() {
        super.onRestart()
        viewModel.restart()
    }

    private fun takeNewPicture(albumId: String, accessToken: String) {
        launchActivity {
            Intent().apply {
                setClass(this@GalleryActivity, CameraActivity::class.java)
                putExtra(ALBUM_ID, albumId)
                putExtra(ACCESS_TOKEN, accessToken)
            }
        }
    }
}