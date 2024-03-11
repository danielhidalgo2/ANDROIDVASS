package com.chat.whatsvass.data.domain.model

data class Login(
    val password: String,
    val login: String,
    val platform: String = "and",
    val firebaseToken: String? = null
)

