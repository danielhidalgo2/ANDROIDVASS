package com.chat.whatsvass.data.domain.repository.remote.response.create_chat

import com.chat.whatsvass.data.domain.model.chat.Chat
import com.chat.whatsvass.data.domain.model.create_chat.NewChat
import com.google.gson.annotations.SerializedName

data class CreatedChatResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("created")
    val created: Boolean,
    @SerializedName("chat")
    val chat: NewChat
)
