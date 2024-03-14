package com.chat.whatsvass.data.domain.repository.remote.mapper

import com.chat.whatsvass.data.domain.model.message.Message
import com.chat.whatsvass.data.domain.repository.remote.response.message.MessagesResponse

class MessageMapper {
    fun fromResponse(response: MessagesResponse): List<Message> {
        return response.rows
    }

    fun toResponse(messages: List<Message>): MessagesResponse {
        return MessagesResponse(messages.size, messages)
    }
}