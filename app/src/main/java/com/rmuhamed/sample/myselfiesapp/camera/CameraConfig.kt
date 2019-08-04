package com.rmuhamed.sample.myselfiesapp.camera

import android.util.Rational
import android.util.Size
import androidx.camera.core.CameraX
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureConfig
import androidx.camera.core.PreviewConfig

fun buildPreviewConfiguration(): PreviewConfig =
    PreviewConfig.Builder()
        .apply {
            setLensFacing(CameraX.LensFacing.FRONT)
            setTargetAspectRatio(Rational(1, 1))
            setTargetResolution(Size(640, 640))
        }.build()

fun buildImageCaptureConfiguration(): ImageCaptureConfig =
    ImageCaptureConfig.Builder()
        .apply {
            setTargetAspectRatio(Rational(1, 1))
            setLensFacing(CameraX.LensFacing.FRONT)
            setTargetResolution(Size(640, 640))
            setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY)
        }.build()