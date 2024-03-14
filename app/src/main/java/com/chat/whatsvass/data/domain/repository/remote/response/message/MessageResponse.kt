package com.chat.whatsvass.data.domain.repository.remote.response.message

import com.chat.whatsvass.data.domain.model.message.Message

data class MessagesResponse(
    val count: Int,
    val rows: List<Message>
)
