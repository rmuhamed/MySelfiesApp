package com.rmuhamed.sample.myselfiesapp

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders

inline fun <reified VM : ViewModel> AppCompatActivity.getViewModel(noinline creator: (() -> VM)?) =
    creator?.let {
        ViewModelProviders.of(this, CustomViewModelFactory(it)).get(VM::class.java)
    } ?: run {
        ViewModelProviders.of(this).get(VM::class.java)
    }