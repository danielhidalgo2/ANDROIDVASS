package com.chat.whatsvass.data.domain.model.contacts

import com.chat.whatsvass.data.domain.repository.remote.response.contacts.ContactsResponse

data class Contacts(
    val users: ContactsResponse
)
