package com.rmuhamed.sample.myselfiesapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


const val ALBUM_ID = "ALBUM_ID"
const val ACCESS_TOKEN = "ACCESS_TOKEN"
const val USER_NAME = "USER_NAME"
const val REQUEST_CODE_PERMISSIONS = 10

val CAMERA_PERMISSION = arrayOf(Manifest.permission.CAMERA)

inline fun <reified VM : ViewModel> AppCompatActivity.getViewModel(noinline creationFunctor: (() -> VM)?) =
    creationFunctor?.let {
        ViewModelProviders.of(this, CustomViewModelFactory(it)).get(VM::class.java)
    } ?: run {
        ViewModelProviders.of(this).get(VM::class.java)
    }

inline fun <reified VM : ViewModel> Fragment.getViewModel(noinline creationFunctor: (() -> VM)?) =
    creationFunctor?.let {
        ViewModelProviders.of(this, CustomViewModelFactory(it)).get(VM::class.java)
    } ?: run {
        ViewModelProviders.of(this).get(VM::class.java)
    }

fun AppCompatActivity.allPermissionsGranted(permissions: Array<String>) =
    permissions.all {
        ContextCompat.checkSelfPermission(this.baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

fun AppCompatActivity.launchFragment(@IdRes containerId: Int, creationFunctor: (() -> Fragment)) {
    supportFragmentManager.beginTransaction().add(containerId, creationFunctor.invoke())
}

fun AppCompatActivity.launchActivity(creationFunctor: () -> Intent) {
    this.startActivity(creationFunctor.invoke())
}

fun Fragment.launchActivity(creationFunctor: () -> Intent) {
    this.startActivity(creationFunctor.invoke())
}

fun AppCompatActivity.showBottomSheetDialog(creationFunctor: (() -> BottomSheetDialogFragment)) {
    creationFunctor.invoke().show(supportFragmentManager, "tag")
}