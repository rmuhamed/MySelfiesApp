package com.rmuhamed.sample.myselfiesapp

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders


const val ALBUM_ID = "ALBUM_ID"
const val ACCESS_TOKEN = "ACCESS_TOKEN"
const val USER_NAME = "USER_NAME"

const val REQUEST_CODE_PERMISSIONS = 10
val CAMERA_PERMISSION = arrayOf(Manifest.permission.CAMERA)

inline fun <reified VM : ViewModel> AppCompatActivity.getViewModel(noinline creator: (() -> VM)?) =
    creator?.let {
        ViewModelProviders.of(this, CustomViewModelFactory(it)).get(VM::class.java)
    } ?: run {
        ViewModelProviders.of(this).get(VM::class.java)
    }

fun AppCompatActivity.allPermissionsGranted(permissions: Array<String>) =
    permissions.all {
        ContextCompat.checkSelfPermission(this.baseContext, it) == PackageManager.PERMISSION_GRANTED
    }