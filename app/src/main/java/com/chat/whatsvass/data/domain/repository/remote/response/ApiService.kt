package com.chat.whatsvass.data.domain.repository.remote.response

import com.chat.whatsvass.data.domain.repository.remote.response.login.LoginResponse
import com.chat.whatsvass.data.domain.model.Login
import com.chat.whatsvass.data.domain.model.logout.Logout
import com.chat.whatsvass.data.domain.model.register.Register
import com.chat.whatsvass.data.domain.repository.remote.response.logout.LogoutResponse
import com.chat.whatsvass.data.domain.repository.remote.response.register.RegisterRequest
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {

    @POST("users/login")
    suspend fun loginUser(@Body request: Login): LoginResponse

    @POST("users/register")
    suspend fun registerUser(@Body post: RegisterRequest): Register

    @POST("users/logout")
    suspend fun logoutUser(@Header("Authorization") token: String): LogoutResponse
}

