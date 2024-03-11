package com.chat.whatsvass.data.domain.repository.remote

import com.chat.whatsvass.data.domain.repository.remote.response.login.LoginResponse
import com.chat.whatsvass.data.domain.repository.remote.mapper.LoginMapper
import com.chat.whatsvass.data.domain.repository.remote.response.ApiService
import com.chat.whatsvass.data.domain.repository.remote.response.RetrofitClient


class UserRepository {
    private val apiService: ApiService = RetrofitClient.apiService

    suspend fun loginUser(username: String, password: String): LoginResponse {
        val request = LoginMapper.mapRequest(username, password)
        return apiService.loginUser(request)
    }
}

