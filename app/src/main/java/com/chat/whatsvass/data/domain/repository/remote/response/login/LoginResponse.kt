package com.chat.whatsvass.data.domain.repository.remote.response.login

import com.chat.whatsvass.data.domain.model.login.User
import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("token")
    val token: String,
    @SerializedName("user")
    val user: User
)

