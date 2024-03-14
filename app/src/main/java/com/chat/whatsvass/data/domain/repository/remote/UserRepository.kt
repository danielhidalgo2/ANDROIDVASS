package com.chat.whatsvass.data.domain.repository.remote

import com.chat.whatsvass.data.domain.model.logout.Logout
import com.chat.whatsvass.data.domain.model.register.Register
import com.chat.whatsvass.data.domain.repository.remote.response.login.LoginResponse
import com.chat.whatsvass.data.domain.repository.remote.mapper.LoginMapper
import com.chat.whatsvass.data.domain.repository.remote.mapper.LogoutMapper
import com.chat.whatsvass.data.domain.repository.remote.mapper.RegisterMapper
import com.chat.whatsvass.data.domain.repository.remote.response.ApiService
import com.chat.whatsvass.data.domain.repository.remote.response.RetrofitClient

class UserRepository {
    private val apiService: ApiService = RetrofitClient.apiService

    suspend fun loginUser(username: String, password: String): LoginResponse {
        val request = LoginMapper().mapRequest(username, password)
        return apiService.loginUser(request)
    }
    suspend fun registerUser(username: String, password: String, nick:String): Register {
        val request = RegisterMapper().mapRequest(username, password, nick)
        return apiService.registerUser(request)
    }
    suspend fun logoutUser(token: String): Logout {
        val request = apiService.logoutUser("$token")
        return LogoutMapper().mapResponse(request)
    }
}

