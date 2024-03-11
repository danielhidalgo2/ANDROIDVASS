package com.chat.whatsvass.data.domain.repository.remote.mapper

import com.chat.whatsvass.data.domain.model.register.RegisterResponse
import com.chat.whatsvass.data.domain.repository.remote.response.register.RegisterRequest

class RegisterMapper {
    fun mapRequest(username: String, nick: String, password: String): RegisterRequest {
        return RegisterRequest(username, nick, password)
    }

    fun mapResponse(response: RegisterResponse): RegisterResponse {
        return RegisterResponse(response.success, response.user)
    }
}
