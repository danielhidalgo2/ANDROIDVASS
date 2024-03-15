package com.chat.whatsvass.data.domain.repository.remote.mapper

import com.chat.whatsvass.data.domain.model.login.Login

class LoginMapper {
    fun mapRequest(username: String, password: String): Login {
        return Login(password, username)
    }

}
