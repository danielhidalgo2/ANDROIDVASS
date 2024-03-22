package com.chat.whatsvass.data.domain.repository.remote.response.message

import com.chat.whatsvass.data.domain.model.message.Message
import com.google.gson.annotations.SerializedName

data class MessagesResponse(
    @SerializedName("count")
    val count: Int,
    @SerializedName("rows")
    val rows: List<Message>
)
