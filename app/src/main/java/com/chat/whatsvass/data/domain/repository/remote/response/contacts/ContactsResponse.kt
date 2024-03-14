package com.chat.whatsvass.data.domain.repository.remote.response.contacts

import com.chat.whatsvass.data.domain.model.register.UserRegister

data class ContactsResponse(
    val users : List<UserRegister>
)
