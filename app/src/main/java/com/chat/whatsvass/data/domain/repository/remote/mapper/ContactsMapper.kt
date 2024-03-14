package com.chat.whatsvass.data.domain.repository.remote.mapper

import com.chat.whatsvass.data.domain.model.contacts.Contacts
import com.chat.whatsvass.data.domain.repository.remote.response.contacts.ContactsResponse

class ContactsMapper {
    fun mapResponse(response: ContactsResponse): Contacts {
        return Contacts(
           response
        )
    }
}


