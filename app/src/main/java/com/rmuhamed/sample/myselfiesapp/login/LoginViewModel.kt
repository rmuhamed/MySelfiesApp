package com.rmuhamed.sample.myselfiesapp.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rmuhamed.sample.myselfiesapp.model.AuthenticatedUser
import com.rmuhamed.sample.myselfiesapp.repository.LoginRepository

class LoginViewModel(private val repo: LoginRepository) : ViewModel() {
    val loginAvailableLiveData = MutableLiveData(false)
    val loginInProgressLiveData = MutableLiveData(false)
    val loginSuccessfulLiveData = MutableLiveData<Boolean?>(null)
    val credentialsInvalidLiveData = MutableLiveData(false)

    var userName = ""
        set(value) = value.run {
            field = value
            loginAvailableLiveData.value = this.isNotBlank()
        }

    fun verifyAccount() {
        if (userName.isNotBlank()) {
            loginInProgressLiveData.value = true
            repo.createToken(
                onSuccess = { checkAccountExistence() },
                onError = { onErrorToBeNotified(message = it) }
            )
        } else {
            loginAvailableLiveData.value = false
        }
    }

    private fun checkAccountExistence() {
        repo.accountExists(userName,
            onSuccess = {
                val userLoggedIn = AuthenticatedUser(accessToken = it, userName = userName)
                repo.saveCredentials(userLoggedIn)
                onSuccessToBeNotified()
            },
            onError = { onErrorToBeNotified(message = it) }
        )
    }

    private fun onSuccessToBeNotified() {
        loginInProgressLiveData.postValue(false);
        loginSuccessfulLiveData.postValue(true)
    }

    private fun onErrorToBeNotified(message: String) {
        loginInProgressLiveData.postValue(false)
        credentialsInvalidLiveData.postValue(true)
    }
}