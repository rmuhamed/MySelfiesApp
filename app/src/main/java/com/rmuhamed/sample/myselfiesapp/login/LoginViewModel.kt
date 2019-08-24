package com.rmuhamed.sample.myselfiesapp.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rmuhamed.sample.myselfiesapp.model.AuthenticatedUser
import com.rmuhamed.sample.myselfiesapp.repository.LoginRepository

class LoginViewModel(private val repo: LoginRepository) : ViewModel() {
    val loginAvailableLiveData = MutableLiveData(false)
    val loginInProgressLiveData = MutableLiveData(false)
    val loginSuccessfulLiveData = MutableLiveData("")
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
                repo.saveCredentials(AuthenticatedUser(accessToken = it, userName = userName))
                loginInProgressLiveData.postValue(false);
                loginSuccessfulLiveData.postValue(it)
            },
            onError = { onErrorToBeNotified(message = it) }
        )
    }

    private fun onErrorToBeNotified(message: String) {
        loginInProgressLiveData.postValue(false)
        credentialsInvalidLiveData.postValue(true)
    }
}