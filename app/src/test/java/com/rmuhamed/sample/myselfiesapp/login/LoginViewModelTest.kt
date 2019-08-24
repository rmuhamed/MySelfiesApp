package com.rmuhamed.sample.myselfiesapp.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.*
import com.rmuhamed.sample.myselfiesapp.api.ImgurAPI
import com.rmuhamed.sample.myselfiesapp.api.dto.BasicResponseDTO
import com.rmuhamed.sample.myselfiesapp.api.dto.TokenResponseDTO
import com.rmuhamed.sample.myselfiesapp.db.MySelfiesDatabase
import com.rmuhamed.sample.myselfiesapp.repository.LoginRepository
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModelTest {
    @Rule
    @JvmField
    var rule: TestRule = InstantTaskExecutorRule()

    private val mockAPI = mock<ImgurAPI>()
    private val mockDB = mock<MySelfiesDatabase>()

    private lateinit var viewModel: LoginViewModel
    private val repo = LoginRepository(mockAPI, mockDB)

    @Before
    fun setUp() {
        repo.accessToken = "accessToken"

        viewModel = LoginViewModel(repo)
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
    fun `check login not available if user name is empty`() {
        viewModel.userName = ""

        assertFalse(viewModel.loginAvailableLiveData.value!!)
    }

    @Test
    fun `check login not available if wants to verify account with empty userName`() {
        viewModel.userName = ""

        viewModel.verifyAccount()

        assertFalse(viewModel.loginAvailableLiveData.value!!)
    }

    @Test
    fun `check retrieve accessToken but services is unavailable`() {
        val mockCreateTokenCall: Call<TokenResponseDTO> = mock()

        val jsonString = "{\n" +
                "  \"data\": {\n" +
                "    \"message\": \"Internal server error\"\n" +
                "  },\n" +
                "  \"success\": false,\n" +
                "  \"status\": 500\n" +
                "}"

        val body = jsonString.toResponseBody(null)

        val tokenResponse = Response.error<TokenResponseDTO>(500, body)
        fakeSuccessRequestEnqueue(mockCreateTokenCall, tokenResponse)

        whenever(mockAPI.createToken(any())).thenReturn(mockCreateTokenCall)

        viewModel.userName = "rmuhamed"

        viewModel.verifyAccount()

        assertFalse(viewModel.loginInProgressLiveData.value!!)
        assertTrue(viewModel.credentialsInvalidLiveData.value!!)
        assertNotNull(viewModel.loginSuccessfulLiveData.value!!)
    }

    @Test
    fun `check retrieve accessToken fails`() {
        val mockCreateTokenCall: Call<TokenResponseDTO> = mock()

        fakeFailedRequestEnqueue(mockCreateTokenCall)

        whenever(mockAPI.createToken(any())).thenReturn(mockCreateTokenCall)

        viewModel.userName = "rmuhamed"

        viewModel.verifyAccount()

        assertFalse(viewModel.loginInProgressLiveData.value!!)
        assertTrue(viewModel.credentialsInvalidLiveData.value!!)
        assertNotNull(viewModel.loginSuccessfulLiveData.value!!)
    }

    @Test
    fun `check login successful`() {
        val mockCreateTokenCall: Call<TokenResponseDTO> = mock()
        val mockLoginCall: Call<BasicResponseDTO<Boolean>> = mock()

        val successfulLoginResponse = Response.success(BasicResponseDTO(true, 200, true))
        val tokenResponse = Response.success(
            TokenResponseDTO(
                "access_token",
                "2000",
                "",
                "refresh_token",
                "account_id",
                "account_username"
            )
        )

        fakeSuccessRequestEnqueue(mockCreateTokenCall, tokenResponse)
        fakeSuccessRequestEnqueue(mockLoginCall, successfulLoginResponse)

        whenever(mockAPI.createToken(any())).thenReturn(mockCreateTokenCall)
        whenever(mockAPI.verifyAccount(any(), eq("rmuhamed"))).thenReturn(mockLoginCall)

        viewModel.userName = "rmuhamed"

        viewModel.verifyAccount()

        assertFalse(viewModel.loginInProgressLiveData.value!!)
        assertFalse(viewModel.credentialsInvalidLiveData.value!!)
        assertNotNull(viewModel.loginSuccessfulLiveData.value!!)
    }

    @Test
    fun `check invalid credentials`() {
        val mockCreateTokenCall: Call<TokenResponseDTO> = mock()
        val mockLoginCall: Call<BasicResponseDTO<Boolean>> = mock()

        val invalidCredentialsResponse = Response.success(BasicResponseDTO(false, 401, false))
        val tokenResponse = Response.success(
            TokenResponseDTO(
                "access_token",
                "2000",
                "",
                "refresh_token",
                "account_id",
                "account_username"
            )
        )

        fakeSuccessRequestEnqueue(mockCreateTokenCall, tokenResponse)
        fakeSuccessRequestEnqueue(mockLoginCall, invalidCredentialsResponse)

        whenever(mockAPI.createToken(any())).thenReturn(mockCreateTokenCall)
        whenever(mockAPI.verifyAccount(any(), eq("rmuhamed"))).thenReturn(mockLoginCall)

        viewModel.userName = "rmuhamed"

        viewModel.verifyAccount()

        assertFalse(viewModel.loginInProgressLiveData.value!!)
        assertTrue(viewModel.credentialsInvalidLiveData.value!!)
        assertEquals("", viewModel.loginSuccessfulLiveData.value!!)
    }

    private fun <T> fakeSuccessRequestEnqueue(someCall: Call<T>, expectedResponse: Response<T>) {
        doAnswer {
            val callback: Callback<T> = it.getArgument(0, Callback::class.java) as Callback<T>
            callback.onResponse(someCall, expectedResponse)
        }.whenever(someCall).enqueue(any())
    }

    private fun <T> fakeFailedRequestEnqueue(someCall: Call<T>) {
        doAnswer {
            val callback: Callback<T> = it.getArgument(0, Callback::class.java) as Callback<T>
            callback.onFailure(someCall, RuntimeException())
        }.whenever(someCall).enqueue(any())
    }
}