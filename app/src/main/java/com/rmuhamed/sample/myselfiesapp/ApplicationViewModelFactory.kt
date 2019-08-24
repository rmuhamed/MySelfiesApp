package com.rmuhamed.sample.myselfiesapp

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rmuhamed.sample.myselfiesapp.application.MySelfiesApplication
import com.rmuhamed.sample.myselfiesapp.repository.IRepository
import com.rmuhamed.sample.myselfiesapp.repository.RepositoryFactory

class ApplicationViewModelFactory(application: Application, repoType: RepositoryFactory.Type) :
    ViewModelProvider.AndroidViewModelFactory(application) {

    private val repo: IRepository

    init {
        val configuration = (application as MySelfiesApplication).configuration
        repo = RepositoryFactory.get(repoType, configuration)
    }

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val repoClass = repo::class.java
        return modelClass.getConstructor(repoClass).newInstance(repo)
    }
}