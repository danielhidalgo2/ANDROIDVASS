package com.chat.whatsvass.data.domain.repository.remote.response.create_message

import com.google.gson.annotations.SerializedName

data class MessageRequest(
    @SerializedName("chat")
    val chat: String,
    @SerializedName("source")
    val source: String,
    @SerializedName("message")
    val message: String
)
