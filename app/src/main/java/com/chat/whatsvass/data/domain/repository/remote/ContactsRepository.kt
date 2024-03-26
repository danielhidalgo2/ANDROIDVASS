package com.chat.whatsvass.data.domain.repository.remote

import com.chat.whatsvass.data.domain.model.contacts.Contacts
import com.chat.whatsvass.data.domain.model.create_chat.CreatedChat
import com.chat.whatsvass.data.domain.repository.remote.mapper.ContactsMapper
import com.chat.whatsvass.data.domain.repository.remote.mapper.CreatedChatMapper
import com.chat.whatsvass.data.domain.repository.remote.response.ApiService
import com.chat.whatsvass.data.domain.repository.remote.response.RetrofitClient
import com.chat.whatsvass.data.domain.repository.remote.response.create_chat.ChatRequest

class ContactsRepository {
    private val apiService: ApiService = RetrofitClient.apiService
    private val contactsMapper = ContactsMapper()
    private val createdChatMapper = CreatedChatMapper()

    suspend fun getContacts(token: String): List<Contacts> {
        val contactsResponses = apiService.getContacts(token)
        return contactsResponses.map { contactsMapper.mapResponse(it) }
    }
    suspend fun createNewChat(token: String, chatRequest: ChatRequest): CreatedChat {
        val createdChatResponses = apiService.createNewChat(token, chatRequest)
        return createdChatMapper.mapResponse(createdChatResponses)
    }
}