package com.chat.whatsvass.data.domain.repository.remote.response.create_chat

import com.google.gson.annotations.SerializedName

data class ChatRequest(
    @SerializedName("source")
    val source: String,
    @SerializedName("target")
    val target: String
)

