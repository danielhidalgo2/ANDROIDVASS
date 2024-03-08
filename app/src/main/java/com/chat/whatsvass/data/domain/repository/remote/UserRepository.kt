package com.chat.whatsvass.data.domain.repository.remote

import com.chat.whatsvass.data.domain.model.Login
import com.chat.whatsvass.data.domain.repository.remote.mapper.LoginMapper
import com.chat.whatsvass.data.domain.repository.remote.response.ApiService
import com.chat.whatsvass.data.domain.repository.remote.response.RetrofitClient
import com.chat.whatsvass.data.domain.repository.remote.response.login.LoginRequest


class UserRepository {

    private val apiService: ApiService = RetrofitClient.apiService

    suspend fun loginUser(loginRequest: LoginRequest): Login {
        return apiService.loginUser(loginRequest)
    }
}

