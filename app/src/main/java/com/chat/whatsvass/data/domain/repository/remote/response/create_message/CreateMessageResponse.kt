package com.chat.whatsvass.data.domain.repository.remote.response.create_message

import com.google.gson.annotations.SerializedName

data class CreateMessageResponse(
    @SerializedName("success")
    val success: Boolean
)