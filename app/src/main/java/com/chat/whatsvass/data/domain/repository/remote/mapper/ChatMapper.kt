package com.chat.whatsvass.data.domain.repository.remote.mapper

import android.os.Build
import androidx.annotation.RequiresApi
import com.chat.whatsvass.data.domain.model.chat.Chat
import com.chat.whatsvass.data.domain.repository.remote.response.chat.ChatResponse
import java.time.LocalDateTime

class ChatMapper {

    @RequiresApi(Build.VERSION_CODES.O)
    fun ChatResponse.toDomainModel(): Chat {
        return Chat(
            chatId = chat,
            sourceId = source,
            sourceNick = sourcenick,
            sourceAvatar = sourceavatar,
            sourceOnline = sourceonline,
            sourceToken = sourcetoken,
            targetId = target,
            targetNick = targetnick,
            targetAvatar = targetavatar,
            targetOnline = targetonline,
            targetToken = targettoken,
            chatCreated = LocalDateTime.parse(chatcreated)
        )
    }
}

