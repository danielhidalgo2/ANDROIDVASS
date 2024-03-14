package com.chat.whatsvass.data.domain.repository.remote

import com.chat.whatsvass.data.domain.model.register.UserRegister
import com.chat.whatsvass.data.domain.repository.remote.mapper.ContactsMapper
import com.chat.whatsvass.data.domain.repository.remote.response.ApiService
import com.chat.whatsvass.data.domain.repository.remote.response.RetrofitClient

class ContactsRepository {
    private val apiService: ApiService = RetrofitClient.apiService

    private val contactsMapper = ContactsMapper()

/*    suspend fun getUsers(token: String): List<UserRegister> {
        val contactsResponses = apiService.getContacts(token)
        return contactsResponses.map { contactsMapper.mapResponse(it) }
    }*/
}
