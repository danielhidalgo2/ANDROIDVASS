package com.chat.whatsvass.data.domain.repository.remote

import android.os.Build
import androidx.annotation.RequiresApi
import com.chat.whatsvass.data.domain.model.chat.Chat
import com.chat.whatsvass.data.domain.repository.remote.mapper.ChatMapper
import com.chat.whatsvass.data.domain.repository.remote.response.ApiService

class ChatRepository(private val apiService: ApiService) {

    private val chatMapper = ChatMapper()

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getChats(token: String): List<Chat> {
        val chatResponses = apiService.getChats("Bearer $token")
        return chatResponses.map { chatMapper.mapResponse(it) }
    }
}


