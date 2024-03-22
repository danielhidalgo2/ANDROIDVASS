package com.chat.whatsvass.data.domain.repository.remote.response.chat

import com.google.gson.annotations.SerializedName

data class ChatResponse(
    @SerializedName("chat")
    val chat: String,
    @SerializedName("source")
    val source: String,
    @SerializedName("sourcenick")
    val sourcenick: String,
    @SerializedName("sourceavatar")
    val sourceavatar: String?,
    @SerializedName("sourceonline")
    val sourceonline: Boolean,
    @SerializedName("sourcetoken")
    val sourcetoken: String?,
    @SerializedName("target")
    val target: String,
    @SerializedName("targetnick")
    val targetnick: String,
    @SerializedName("targetavatar")
    val targetavatar: String?,
    @SerializedName("targetonline")
    val targetonline: Boolean,
    @SerializedName("targettoken")
    val targettoken: String?,
    @SerializedName("chatcreated")
    val chatcreated: String
)
