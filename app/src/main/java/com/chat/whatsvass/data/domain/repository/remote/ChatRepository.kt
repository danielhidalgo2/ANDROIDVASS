package com.chat.whatsvass.data.domain.repository.remote

import com.chat.whatsvass.data.domain.model.chat.Chat
import com.chat.whatsvass.data.domain.model.create_message.CreateMessage
import com.chat.whatsvass.data.domain.model.message.Message
import com.chat.whatsvass.data.domain.repository.remote.mapper.ChatMapper
import com.chat.whatsvass.data.domain.repository.remote.mapper.CreatedMessageMapper
import com.chat.whatsvass.data.domain.repository.remote.mapper.MessageMapper
import com.chat.whatsvass.data.domain.repository.remote.response.ApiService
import com.chat.whatsvass.data.domain.repository.remote.response.RetrofitClient
import com.chat.whatsvass.data.domain.repository.remote.response.create_message.MessageRequest

class ChatRepository {
    private val apiService: ApiService = RetrofitClient.apiService

    private val chatMapper = ChatMapper()
    private val messageMapper = MessageMapper() // Agregado
    private val createdMessageMapper = CreatedMessageMapper()


    suspend fun getChats(token: String): List<Chat> {
        val chatResponses = apiService.getChats(token)
        return chatResponses.map { chatMapper.mapResponse(it) }
    }

    suspend fun getMessages(token: String, chatId: String, offset: Int, limit: Int): List<Message> {
        val messagesResponse = apiService.getMessages(chatId, offset, limit, token)
        return messageMapper.fromResponse(messagesResponse)
    }

    suspend fun deleteChat(token: String, chatId: String) {
        try {
            apiService.deleteChat(chatId, token)
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun createNewMessage(token: String, messageRequest: MessageRequest): CreateMessage {
        val createdMessageResponses = apiService.createNewMessage(token, messageRequest)
        return createdMessageMapper.mapResponse(createdMessageResponses)
    }
}

