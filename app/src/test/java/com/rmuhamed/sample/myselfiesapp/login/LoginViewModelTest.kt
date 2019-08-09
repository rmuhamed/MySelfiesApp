package com.rmuhamed.sample.myselfiesapp.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doNothing
import com.nhaarman.mockitokotlin2.mock
import com.rmuhamed.sample.myselfiesapp.repository.LoginRepository
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class LoginViewModelTest {
    @Rule
    @JvmField
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var viewModel: LoginViewModel
    val mockRepo = mock<LoginRepository>()

    @Before
    fun setUp() {
        viewModel = LoginViewModel(mockRepo)
    }

    @Test
    fun `check liveData initial state`() {
        assertFalse(viewModel.loginAvailableLiveData.value!!)
        assertFalse(viewModel.loginInProgressLiveData.value!!)
        assertFalse(viewModel.credentialsInvalidLiveData.value!!)
        assertTrue(viewModel.loginSuccessfulLiveData.value.isNullOrBlank())
    }

    @Test
    fun `check login available if user name is present`() {
        viewModel.userName = "Ricardo Muhamed"

        assertTrue(viewModel.loginAvailableLiveData.value!!)
    }

    @Test
    fun `check login in progress notification`() {
        doNothing().`when`(mockRepo).accountExists(any(), any(), any())
        viewModel.userName = "Ricardo Muhamed"

        viewModel.verifyAccount()

        assertTrue(viewModel.loginInProgressLiveData.value!!)

    }

    @Test
    fun `check invalid credentials`() {
    }
}