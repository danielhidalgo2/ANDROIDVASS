package com.chat.whatsvass.data.domain.model.create_chat

import com.chat.whatsvass.data.domain.model.chat.Chat

data class CreatedChat(
    val success: Boolean,
    val created: Boolean,
    val chat: NewChat
)
