package com.chat.whatsvass.data.domain.repository.remote.response

import com.chat.whatsvass.data.domain.repository.remote.response.login.LoginResponse
import com.chat.whatsvass.data.domain.model.Login
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("users/login")
    suspend fun loginUser(@Body request: Login): LoginResponse
}

