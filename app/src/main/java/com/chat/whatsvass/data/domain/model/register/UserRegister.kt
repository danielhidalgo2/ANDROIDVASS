package com.chat.whatsvass.data.domain.model.register

data class UserRegister(
    val id: String,
    val login: String,
    val password: String,
    val nick: String,
    val platform: String,
    val avatar: String,
    val uuid: String,
    val token: String,
    val online:Boolean,
    val created: String,
    val updated: String
)
