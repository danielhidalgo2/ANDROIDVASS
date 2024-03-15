package com.chat.whatsvass.data.domain.model.login

data class Login(
    val password: String,
    val login: String,
    val platform: String = "and",
    val firebaseToken: String? = null
)

