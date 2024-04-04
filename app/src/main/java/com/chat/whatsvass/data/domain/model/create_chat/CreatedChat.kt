package com.chat.whatsvass.data.domain.model.create_chat

data class CreatedChat(
    val success: Boolean,
    val created: Boolean,
    val chat: NewChat
)
