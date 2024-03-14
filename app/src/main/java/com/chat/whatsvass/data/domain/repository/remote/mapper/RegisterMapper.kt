package com.chat.whatsvass.data.domain.repository.remote.mapper

import com.chat.whatsvass.data.domain.repository.remote.response.register.RegisterResponse

class RegisterMapper {
    fun mapRequest(username: String, password: String, nick: String): RegisterResponse {
        return RegisterResponse(username, password, nick)
    }

}
