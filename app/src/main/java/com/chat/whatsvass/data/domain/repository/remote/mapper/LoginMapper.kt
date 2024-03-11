package com.chat.whatsvass.data.domain.repository.remote.mapper

import com.chat.whatsvass.data.domain.model.LoginResponse
import com.chat.whatsvass.data.domain.model.User
import com.chat.whatsvass.data.domain.repository.remote.response.login.LoginRequest

object LoginMapper {
    fun mapRequest(username: String, password: String): LoginRequest {
        return LoginRequest(password, username)
    }

    fun mapResponse(response: LoginResponse): LoginResponse {
        return LoginResponse(response.token, response.user)
    }
}
