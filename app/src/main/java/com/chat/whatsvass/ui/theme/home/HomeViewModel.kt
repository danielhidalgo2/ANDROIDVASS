package com.chat.whatsvass.ui.theme.home

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chat.whatsvass.data.domain.model.chat.Chat
import com.chat.whatsvass.data.domain.repository.remote.ChatRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    sealed class ChatResult {
        data class Success(val chats: List<Chat>) : ChatResult()
        data class Error(val message: String) : ChatResult()
    }

    private val chatRepository = ChatRepository()

    private val _chatResult = MutableStateFlow<ChatResult?>(null)
    val chatResult: StateFlow<ChatResult?> = _chatResult

    fun getChats(token: String) {
        viewModelScope.launch {
            try {
                val chats = chatRepository.getChats(token)
                _chatResult.value = ChatResult.Success(chats)
                Log.d("HomeViewModel", token)

            } catch (e: Exception) {
                _chatResult.value = ChatResult.Error("Error al obtener los chats: ${e.message}")
                Log.d("HomeViewModel", token)

                Log.e("HomeViewModel", "Error fetching chats", e)

            }
        }
    }
}
