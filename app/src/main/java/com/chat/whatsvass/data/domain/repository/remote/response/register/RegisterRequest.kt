package com.chat.whatsvass.data.domain.repository.remote.response.register

data class RegisterRequest(
    val login: String,
    val password: String,
    val nick: String,
    val platform: String = "and",
    val firebaseToken: String? = null
)

