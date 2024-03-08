package com.chat.whatsvass.data.domain.repository.remote.response

import com.chat.whatsvass.data.domain.model.LoginResponse
import com.chat.whatsvass.data.domain.repository.remote.response.login.LoginRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("users/login")
    suspend fun loginUser(@Body request: LoginRequest): LoginResponse
}

