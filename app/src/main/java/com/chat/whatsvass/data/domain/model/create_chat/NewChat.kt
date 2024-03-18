package com.chat.whatsvass.data.domain.model.create_chat

data class NewChat(
    val id: String,
    val source : String,
    val target : String,
    val created : String
)
