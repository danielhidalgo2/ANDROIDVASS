package com.chat.whatsvass.data.domain.repository.remote

import com.chat.whatsvass.data.domain.model.chat.Chat
import com.chat.whatsvass.data.domain.model.contacts.Contacts
import com.chat.whatsvass.data.domain.repository.remote.mapper.ChatMapper
import com.chat.whatsvass.data.domain.repository.remote.mapper.ContactsMapper
import com.chat.whatsvass.data.domain.repository.remote.response.ApiService
import com.chat.whatsvass.data.domain.repository.remote.response.RetrofitClient

class ContactsRepository {
    private val apiService: ApiService = RetrofitClient.apiService
    private val contactsMapper = ContactsMapper()

    suspend fun getContacts(token: String): List<Contacts> {
        val chatResponses = apiService.getContacts(token)
        return chatResponses.map { contactsMapper.mapResponse(it) }
    }
}