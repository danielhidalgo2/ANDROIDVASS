package com.chat.whatsvass.data.domain.repository.remote.mapper

import com.chat.whatsvass.data.domain.model.create_chat.CreatedChat
import com.chat.whatsvass.data.domain.model.create_message.CreateMessage
import com.chat.whatsvass.data.domain.repository.remote.response.create_chat.CreatedChatResponse
import com.chat.whatsvass.data.domain.repository.remote.response.create_message.CreateMessageResponse

class CreatedMessageMapper {
    fun mapResponse(response: CreateMessageResponse): CreateMessage {
        return CreateMessage(
            response.success
        )
    }
}