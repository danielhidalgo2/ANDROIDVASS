package com.chat.whatsvass.data.domain.model.chat


data class Chat(
    val chatId: String,
    val sourceId: String,
    val sourceNick: String,
    val sourceAvatar: String?,
    val sourceOnline: Boolean,
    val sourceToken: String?,
    val targetId: String,
    val targetNick: String,
    val targetAvatar: String?,
    val targetOnline: Boolean,
    val targetToken: String?,
    val chatCreated: String
)

