package com.chat.whatsvass.data.domain.model.register

import com.chat.whatsvass.data.domain.model.User

data class RegisterResponse(
    val success: Boolean,
    val user: User
)

