package com.chat.whatsvass.data.domain.repository.remote.response.contacts

import com.chat.whatsvass.data.domain.model.register.UserRegister

data class ContactsResponse(
    val id: String,
    val login: String,
    val password: String,
    val nick: String,
    val avatar: String,
    val platform: String,
    val uuid: String,
    val token: String,
    val online:Boolean,
    val created: String,
    val updated: String
)
