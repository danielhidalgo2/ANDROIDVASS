package com.chat.whatsvass.data.domain.repository.remote

import com.chat.whatsvass.data.domain.model.chat.Chat
import com.chat.whatsvass.data.domain.model.message.Message
import com.chat.whatsvass.data.domain.repository.remote.mapper.ChatMapper
import com.chat.whatsvass.data.domain.repository.remote.mapper.MessageMapper
import com.chat.whatsvass.data.domain.repository.remote.response.ApiService
import com.chat.whatsvass.data.domain.repository.remote.response.RetrofitClient
class ChatRepository {
    private val apiService: ApiService = RetrofitClient.apiService

    private val chatMapper = ChatMapper()
    private val messageMapper = MessageMapper() // Agregado

    suspend fun getChats(token: String): List<Chat> {
        val chatResponses = apiService.getChats(token)
        return chatResponses.map { chatMapper.mapResponse(it) }
    }

    suspend fun getMessages(token: String, chatId: String, offset: Int, limit: Int): List<Message> {
        val messagesResponse = apiService.getMessages(chatId, offset, limit, token)
        return messageMapper.fromResponse(messagesResponse) // Mapeo de respuesta
    }
}

