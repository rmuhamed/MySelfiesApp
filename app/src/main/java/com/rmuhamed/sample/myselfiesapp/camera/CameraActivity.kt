package com.rmuhamed.sample.myselfiesapp.camera

import android.graphics.Matrix
import android.os.Bundle
import android.util.Rational
import android.util.Size
import android.view.Surface
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_SHORT
import com.google.android.material.snackbar.Snackbar
import com.rmuhamed.sample.myselfiesapp.*
import com.rmuhamed.sample.myselfiesapp.R
import com.rmuhamed.sample.myselfiesapp.repository.CameraRepository
import kotlinx.android.synthetic.main.activity_camera.*
import java.io.File

class CameraActivity : AppCompatActivity() {
    private lateinit var viewModel: CameraViewModel
    private lateinit var imageCaptureConfig: ImageCaptureConfig
    private lateinit var previewConfig: PreviewConfig
    private lateinit var viewFinder: TextureView

    private lateinit var imageCapture: ImageCapture

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        viewFinder = findViewById(R.id.view_finder)

        val albumId = intent.getStringExtra("ALBUM_ID")
        val accessToken = intent.getStringExtra("ACCESS_TOKEN")

        viewModel = getViewModel { CameraViewModel(CameraRepository(albumId, accessToken)) }

        previewConfig = buildPreviewConfiguration()
        imageCaptureConfig = buildImageCaptureConfiguration()

        if (allPermissionsGranted(CAMERA_PERMISSION)) {
            viewFinder.post { initCamera(previewConfig, imageCaptureConfig) }
        } else {
            ActivityCompat.requestPermissions(
                this,
                CAMERA_PERMISSION,
                REQUEST_CODE_PERMISSIONS
            )
        }

        viewModel.uploading.observe(this, Observer {
            if (it) progress.visibility = View.VISIBLE else View.GONE
        })

        viewModel.successLiveData.observe(this, Observer {
            Snackbar.make(capture_button, "Successfully uploaed", LENGTH_SHORT).show()
        })

        viewModel.errorLiveData.observe(this, Observer {
            it?.let {
                Snackbar.make(capture_button, it, LENGTH_SHORT).show()
            }
        })

        // Every time the provided texture view changes, recompute layout
        viewFinder.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            updateTransform()
        }

        capture_button.setOnClickListener {
            captureNewPicture()
        }
    }

    private fun captureNewPicture() {
        val file = File(
            externalMediaDirs.first(),
            "${System.currentTimeMillis()}.jpg"
        )

        imageCapture.takePicture(file,
            object : ImageCapture.OnImageSavedListener {
                override fun onError(
                    error: ImageCapture.UseCaseError,
                    message: String, exc: Throwable?
                ) {
                    val msg = "Photo capture failed: $message"
                    Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                    exc?.printStackTrace()
                }

                override fun onImageSaved(file: File) {
                    val msg = "Photo capture succeeded: ${file.absolutePath}"
                    Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()

                    viewModel.doUpload(file, "Picture", "Title", "Description")
                }
            })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted(CAMERA_PERMISSION)) {
                viewFinder.post { initCamera(previewConfig, imageCaptureConfig) }
            } else {
                Toast.makeText(
                    this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun initCamera(
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

    private fun buildPreviewConfiguration(): PreviewConfig =
        PreviewConfig.Builder()
            .apply {
                setLensFacing(CameraX.LensFacing.FRONT)
                setTargetAspectRatio(Rational(1, 1))
                setTargetResolution(Size(640, 640))
            }.build()

    private fun buildImageCaptureConfiguration(): ImageCaptureConfig =
        ImageCaptureConfig.Builder()
            .apply {
                setTargetAspectRatio(Rational(1, 1))
                setLensFacing(CameraX.LensFacing.FRONT)
                setTargetResolution(Size(640, 640))
                setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY)
            }.build()
}

