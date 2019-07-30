package com.rmuhamed.sample.myselfiesapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CustomViewModelFactory<VM>(private val creator: () -> VM) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = creator.invoke() as T
}