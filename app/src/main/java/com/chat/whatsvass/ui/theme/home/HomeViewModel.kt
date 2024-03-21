package com.chat.whatsvass.ui.theme.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chat.whatsvass.data.domain.model.chat.Chat
import com.chat.whatsvass.data.domain.model.message.Message
import com.chat.whatsvass.data.domain.repository.remote.ChatRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import com.chat.whatsvass.data.domain.repository.remote.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val chatRepository = ChatRepository()
    private val userRepository = UserRepository()

    private val _chats = MutableStateFlow<List<Chat>>(emptyList())
    val chats: StateFlow<List<Chat>> = _chats

    private val _messages = MutableStateFlow<Map<String, List<Message>>>(emptyMap())
    val messages: StateFlow<Map<String, List<Message>>> = _messages

    fun getChats(token: String) {
        viewModelScope.launch {
            try {
                val chats = chatRepository.getChats(token)
                _chats.value = chats
                // Actualiza el estado en línea del usuario al obtener los chats
                userRepository.updateUserOnlineStatus(token, true)
                Log.d("HomeViewModel", "Chats obtenidos correctamente")
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error al obtener los chats", e)
            }
        }
    }

    suspend fun getMessages(token: String, chatIds: List<String>, offset: Int, limit: Int) {
        viewModelScope.launch {
            try {
                val messagesMap = mutableMapOf<String, List<Message>>()
                chatIds.forEach { chatId ->
                    val messages = chatRepository.getMessages(token, chatId, offset, limit)
                    messagesMap[chatId] = messages
                }
                _messages.value = messagesMap
                Log.d("HomeViewModel", "Mensajes obtenidos correctamente")
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error al obtener los mensajes", e)
            }
        }
    }

    fun deleteChat(token: String?, chatId: String) {
        viewModelScope.launch {
            try {
                if (token != null) {
                    chatRepository.deleteChat(token, chatId)
                    // Una vez eliminado el chat, actualiza la lista de chats
                    val updatedChats = _chats.value.filter { it.chatId != chatId }
                    _chats.value = updatedChats
                    Log.d("HomeViewModel", "Chat eliminado correctamente")
                } else {
                    Log.e("HomeViewModel", "No se proporcionó un token válido")
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error al eliminar el chat", e)
            }
        }
    }

    fun updateUserOnlineStatus(token: String, isOnline: Boolean) {
        viewModelScope.launch {
            try {
                userRepository.updateUserOnlineStatus(token, isOnline)
                Log.d("HomeViewModel", "Estado en línea del usuario actualizado correctamente")
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error al actualizar el estado en línea del usuario", e)
            }
        }
    }

}



