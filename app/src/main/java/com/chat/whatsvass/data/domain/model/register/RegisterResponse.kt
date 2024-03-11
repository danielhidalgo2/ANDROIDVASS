package com.chat.whatsvass.data.domain.model.register

import com.chat.whatsvass.data.domain.model.User
import com.chat.whatsvass.data.domain.repository.remote.UserRepository

data class RegisterResponse(
    val success: Boolean,
    val user: UserRegister
)

