package com.chat.whatsvass.data.domain.repository.remote.response.create_message

data class MessageRequest(
    val chat: String,
    val source: String,
    val message: String
)
