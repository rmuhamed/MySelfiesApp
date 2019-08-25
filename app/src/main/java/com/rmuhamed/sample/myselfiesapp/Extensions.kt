package com.rmuhamed.sample.myselfiesapp

import android.Manifest
import android.app.Application
import android.content.Intent
import android.content.pm.PackageManager
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rmuhamed.sample.myselfiesapp.repository.RepositoryFactory


const val ALBUM_ID = "ALBUM_ID"
const val ACCESS_TOKEN = "ACCESS_TOKEN"
const val USER_NAME = "USER_NAME"
const val REQUEST_CODE_PERMISSIONS = 10

val CAMERA_PERMISSION = arrayOf(Manifest.permission.CAMERA)

fun <T : ViewModel> AppCompatActivity.getViewModel(
    modelClass: Class<T>,
    application: Application,
    repoType: RepositoryFactory.Type
) =
    ViewModelProvider(this, ApplicationViewModelFactory(application, repoType)).get(modelClass)

fun <T : ViewModel> Fragment.getViewModel(
    modelClass: Class<T>,
    application: Application,
    repoType: RepositoryFactory.Type
) =
    ViewModelProvider(this, ApplicationViewModelFactory(application, repoType)).get(modelClass)

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