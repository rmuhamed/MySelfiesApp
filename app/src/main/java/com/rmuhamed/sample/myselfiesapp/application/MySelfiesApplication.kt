package com.rmuhamed.sample.myselfiesapp.application

import android.app.Application

class MySelfiesApplication : Application() {

    lateinit var configuration: Configuration

    override fun onCreate() {
        super.onCreate()

        configuration = Configuration()
    }
}