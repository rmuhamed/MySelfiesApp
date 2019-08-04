package com.rmuhamed.sample.myselfiesapp.camera

import android.graphics.Matrix
import android.os.Bundle
import android.view.Surface
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_SHORT
import com.google.android.material.snackbar.Snackbar
import com.rmuhamed.sample.myselfiesapp.*
import com.rmuhamed.sample.myselfiesapp.R
import com.rmuhamed.sample.myselfiesapp.repository.CameraRepository
import com.rmuhamed.sample.myselfiesapp.view.dialogForPictureMetadata
import kotlinx.android.synthetic.main.activity_camera.*

class CameraActivity : AppCompatActivity() {
    private lateinit var viewModel: CameraViewModel
    private lateinit var viewFinder: TextureView

    private lateinit var imageCapture: ImageCapture

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        val albumId = intent.getStringExtra(ALBUM_ID) ?: ""
        val accessToken = intent.getStringExtra(ACCESS_TOKEN) ?: ""

        viewFinder = view_finder

        viewModel = getViewModel { CameraViewModel(CameraRepository(albumId, accessToken)) }

        if (allPermissionsGranted(CAMERA_PERMISSION)) {
            viewFinder.post { cameraInit(buildPreviewConfiguration(), buildImageCaptureConfiguration()) }
        } else {
            ActivityCompat.requestPermissions(
                this,
                CAMERA_PERMISSION,
                REQUEST_CODE_PERMISSIONS
            )
        }

        viewModel.uploadingLiveData.observe(this, Observer {
            progress.visibility = if (it) View.VISIBLE else View.GONE
        })

        viewModel.uploadedLiveData.observe(this, Observer {
            Snackbar.make(capture_button, R.string.camera_upload_successful, LENGTH_SHORT).show()
            this.finish()
        })

        viewModel.someErrorLiveData.observe(this, Observer {
            it?.let {
                Snackbar.make(capture_button, it, LENGTH_SHORT).show()
            }
        })

        viewModel.capturedPictureSucceed.observe(this, Observer {
            it?.let {
                dialogForPictureMetadata(
                    layoutId = R.layout.picture_metada_dialog,
                    context = this,
                    action = { name, title, description -> viewModel.uploadPicture(it, name, title, description) }
                ).show()
            }
        })

        // Every time the provided texture view changes, recompute layout
        viewFinder.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            updateTransform()
        }

        capture_button.setOnClickListener {
            viewModel.captureNewPicture(imageCapture, albumId, System.currentTimeMillis(), externalMediaDirs.first())
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted(CAMERA_PERMISSION)) {
                viewFinder.post { cameraInit(buildPreviewConfiguration(), buildImageCaptureConfiguration()) }
            } else {
                Snackbar.make(
                    capture_button,
                    R.string.camera_permissions_not_granted,
                    LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun cameraInit(
        previewConfiguration: PreviewConfig,
        imageCaptureConfig: ImageCaptureConfig
    ) {

        val preview = Preview(previewConfiguration)
        preview.setOnPreviewOutputUpdateListener {
            // To update the SurfaceTexture, we have to remove it and re-add it
            val parent = viewFinder.parent as ViewGroup
            parent.removeView(viewFinder)
            parent.addView(viewFinder, 0)

            viewFinder.surfaceTexture = it.surfaceTexture
            updateTransform()
        }

        imageCapture = ImageCapture(imageCaptureConfig)

        CameraX.bindToLifecycle(this, preview, imageCapture)
    }

    private fun updateTransform() {
        val matrix = Matrix()

        // Compute the center of the view finder
        val centerX = viewFinder.width / 2f
        val centerY = viewFinder.height / 2f

        // Correct preview output to account for display rotation
        val rotationDegrees = when (viewFinder.display.rotation) {
            Surface.ROTATION_0 -> 0
            Surface.ROTATION_90 -> 90
            Surface.ROTATION_180 -> 180
            Surface.ROTATION_270 -> 270
            else -> return
        }
        matrix.postRotate(-rotationDegrees.toFloat(), centerX, centerY)

        // Finally, apply transformations to our TextureView
        viewFinder.setTransform(matrix)
    }
}

