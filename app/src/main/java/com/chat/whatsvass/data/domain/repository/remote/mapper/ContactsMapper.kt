package com.chat.whatsvass.data.domain.repository.remote.mapper

import com.chat.whatsvass.data.domain.model.contacts.Contacts
import com.chat.whatsvass.data.domain.repository.remote.response.contacts.ContactsResponse

class ContactsMapper {

    fun mapResponse(response: ContactsResponse): Contacts {
        return Contacts(
            response.id,
            response.login,
            response.password,
            response.nick,
            response.platform,
            response.avatar,
            response.uuid,
            response.token,
            response.online,
            response.created,
            response.updated
        )
    }
}


