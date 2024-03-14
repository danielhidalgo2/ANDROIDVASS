package com.chat.whatsvass.data.domain.repository.remote.response.chat

data class ChatResponse(
    val chat: String,
    val source: String,
    val sourcenick: String,
    val sourceavatar: String?,
    val sourceonline: Boolean,
    val sourcetoken: String?,
    val target: String,
    val targetnick: String,
    val targetavatar: String?,
    val targetonline: Boolean,
    val targettoken: String?,
    val chatcreated: String
)
