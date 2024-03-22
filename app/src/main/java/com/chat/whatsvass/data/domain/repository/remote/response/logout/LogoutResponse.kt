package com.chat.whatsvass.data.domain.repository.remote.response.logout

import com.google.gson.annotations.SerializedName

data class LogoutResponse(
    @SerializedName("message")
    val message: String
)

