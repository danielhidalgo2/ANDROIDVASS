package com.chat.whatsvass.data.domain.model

data class LoginResponse(
    val token: String,
    val user: User
)

