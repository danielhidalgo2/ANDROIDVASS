package com.chat.whatsvass.data.domain.model.login

data class User(
    val id: String,
    val nick: String,
    val avatarUrl: String?,
    val online: Boolean
)

