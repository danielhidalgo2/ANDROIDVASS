package com.chat.whatsvass.data.domain.model.message

data class Message(
    val id: String,
    val chat: String,
    val source: String,
    val message: String,
    val date: String
)
