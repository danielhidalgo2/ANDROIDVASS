package com.chat.whatsvass.data.domain.repository.remote.response.login

import com.chat.whatsvass.data.domain.model.User

data class LoginResponse(
    val token: String,
    val user: User
)

