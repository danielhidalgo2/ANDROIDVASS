package com.chat.whatsvass.data.domain.repository.remote.response.login

data class LoginRequest(
    val password: String,
    val login: String,
    val platform: String = "and",
    val firebaseToken: String? = null
)

