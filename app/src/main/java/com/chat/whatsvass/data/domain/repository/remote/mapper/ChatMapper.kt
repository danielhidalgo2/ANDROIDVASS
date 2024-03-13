package com.chat.whatsvass.data.domain.repository.remote.mapper

import android.os.Build
import androidx.annotation.RequiresApi
import com.chat.whatsvass.data.domain.model.chat.Chat
import com.chat.whatsvass.data.domain.repository.remote.response.chat.ChatResponse
import java.time.LocalDateTime

class ChatMapper {

    @RequiresApi(Build.VERSION_CODES.O)
    fun mapResponse(response: ChatResponse): Chat {
        return Chat(
            chatId = response.chat,
            sourceId = response.source,
            sourceNick = response.sourcenick,
            sourceAvatar = response.sourceavatar,
            sourceOnline = response.sourceonline,
            sourceToken = response.sourcetoken,
            targetId = response.target,
            targetNick = response.targetnick,
            targetAvatar = response.targetavatar,
            targetOnline = response.targetonline,
            targetToken = response.targettoken,
            chatCreated = LocalDateTime.parse(response.chatcreated)
        )
    }
}


