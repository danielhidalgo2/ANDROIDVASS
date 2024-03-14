package com.chat.whatsvass.data.domain.repository.remote

import android.os.Build
import androidx.annotation.RequiresApi
import com.chat.whatsvass.data.domain.model.chat.Chat
import com.chat.whatsvass.data.domain.repository.remote.mapper.ChatMapper
import com.chat.whatsvass.data.domain.repository.remote.response.ApiService
import com.chat.whatsvass.data.domain.repository.remote.response.RetrofitClient

class ChatRepository {
    private val apiService: ApiService = RetrofitClient.apiService

    private val chatMapper = ChatMapper()

    suspend fun getChats(token: String): List<Chat> {
        val chatResponses = apiService.getChats(token)
        return chatResponses.map { chatMapper.mapResponse(it) }
    }
}
