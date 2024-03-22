package com.chat.whatsvass.data.domain.repository.remote.response.register

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("login")
    val login: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("nick")
    val nick: String,
    @SerializedName("platform")
    val platform: String = "and",
    @SerializedName("firebaseToken")
    val firebaseToken: String? = null
)

