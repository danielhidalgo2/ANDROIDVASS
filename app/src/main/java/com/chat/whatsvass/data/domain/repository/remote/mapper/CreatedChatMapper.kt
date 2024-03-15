package com.chat.whatsvass.data.domain.repository.remote.mapper

import com.chat.whatsvass.data.domain.model.contacts.Contacts
import com.chat.whatsvass.data.domain.model.create_chat.CreatedChat
import com.chat.whatsvass.data.domain.model.message.Message
import com.chat.whatsvass.data.domain.repository.remote.response.chat.ChatResponse
import com.chat.whatsvass.data.domain.repository.remote.response.contacts.ContactsResponse
import com.chat.whatsvass.data.domain.repository.remote.response.create_chat.CreatedChatResponse
import com.chat.whatsvass.data.domain.repository.remote.response.message.MessagesResponse

class CreatedChatMapper {
    fun mapResponse(response: CreatedChatResponse): CreatedChat {
        return CreatedChat(
            response.success,
            response.created,
            response.chat
        )
    }
}


