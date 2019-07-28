package com.rmuhamed.sample.myselfiesapp.camera

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Matrix
import android.os.Bundle
import android.util.Rational
import android.util.Size
import android.view.Surface
import android.view.TextureView
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.rmuhamed.sample.myselfiesapp.R
import java.io.File

private const val REQUEST_CODE_PERMISSIONS = 10

private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)

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

        viewModel = ViewModelProviders.of(this).get(CameraViewModel::class.java)

        previewConfig = buildPreviewConfiguration()
        imageCaptureConfig = buildImageCaptureConfiguration()

        if (allPermissionsGranted()) {
            viewFinder.post { initCamera(previewConfig, imageCaptureConfig) }
        } else {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        // Every time the provided texture view changes, recompute layout
        viewFinder.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            updateTransform()
        }

        findViewById<ImageButton>(R.id.capture_button).setOnClickListener {
            handleNewSelfie(albumId)
        }
    }

    private fun handleNewSelfie(albumId: String) {
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

                    viewModel.upload(albumId, file)
                }
            })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
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

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it
        ) == PackageManager.PERMISSION_GRANTED
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

