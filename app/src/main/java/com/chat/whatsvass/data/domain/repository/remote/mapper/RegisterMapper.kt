package com.chat.whatsvass.data.domain.repository.remote.mapper

import com.chat.whatsvass.data.domain.model.register.Register
import com.chat.whatsvass.data.domain.repository.remote.response.register.RegisterRequest

class RegisterMapper {
    fun mapRequest(username: String, password: String, nick: String): RegisterRequest {
        return RegisterRequest(username, password, nick)
    }

    fun mapResponse(response: Register): Register {
        return Register(response.success, response.user)
    }
}
