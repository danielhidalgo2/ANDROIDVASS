package com.chat.whatsvass.ui.theme.home

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chat.whatsvass.data.domain.model.chat.Chat
import com.chat.whatsvass.data.domain.model.message.Message
import com.chat.whatsvass.data.domain.repository.remote.ChatRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    sealed class ChatResult {
        data class Success(val chats: List<Chat>, val messages: List<Message>) : ChatResult()
        data class Error(val message: String) : ChatResult()
    }

    private val chatRepository = ChatRepository()

    private val _chatResult = MutableStateFlow<ChatResult?>(null)
    val chatResult: StateFlow<ChatResult?> = _chatResult

    fun getChats(token: String) {
        viewModelScope.launch {
            try {
                val chats = chatRepository.getChats(token)
                _chatResult.value = ChatResult.Success(chats, emptyList()) // Llamamos a Success con una lista de mensajes vacía
                Log.d("HomeViewModel", "Chats obtenidos correctamente")

            } catch (e: Exception) {
                _chatResult.value = ChatResult.Error("Error al obtener los chats: ${e.message}")
                Log.e("HomeViewModel", "Error al obtener los chats", e)
            }
        }
    }

    // Nuevo método para obtener los mensajes
    fun getMessages(token: String, chatId: Int, offset: Int, limit: Int) {
        viewModelScope.launch {
            try {
                val messages = chatRepository.getMessages(token, chatId, offset, limit)
                _chatResult.value = ChatResult.Success(emptyList(), messages) // Llamamos a Success con una lista de chats vacía y los mensajes obtenidos
                Log.d("HomeViewModel", "Mensajes obtenidos correctamente")

            } catch (e: Exception) {
                _chatResult.value = ChatResult.Error("Error al obtener los mensajes: ${e.message}")
                Log.e("HomeViewModel", "Error al obtener los mensajes", e)
            }
        }
    }
}

