package com.chat.whatsvass.data.domain.repository.remote.response.create_chat

import com.chat.whatsvass.data.domain.model.chat.Chat
import com.chat.whatsvass.data.domain.model.create_chat.NewChat

data class CreatedChatResponse(
    val success: Boolean,
    val created: Boolean,
    val chat: NewChat
)
