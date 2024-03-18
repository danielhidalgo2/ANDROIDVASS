package com.chat.whatsvass.ui.theme.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chat.whatsvass.data.domain.model.chat.Chat
import com.chat.whatsvass.data.domain.model.message.Message
import com.chat.whatsvass.data.domain.repository.remote.ChatRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val chatRepository = ChatRepository()

    private val isTextWithOutChatsVisible = MutableStateFlow(false)
    var isTextWithOutChatsVisibleFlow: Flow<Boolean> = isTextWithOutChatsVisible

    private val _chats = MutableStateFlow<List<Chat>>(emptyList())
    val chats: StateFlow<List<Chat>> = _chats

    private val _messages = MutableStateFlow<Map<String, List<Message>>>(emptyMap())
    val messages: StateFlow<Map<String, List<Message>>> = _messages

    fun getChats(token: String){
        viewModelScope.launch {
            async {
                getChatsList(token)
            }.await()
            if (_chats.value.isEmpty()){
                isTextWithOutChatsVisible.value = true
            }
        }
    }
    private suspend fun getChatsList(token: String) {
            try {
                val chats = chatRepository.getChats(token)
                _chats.value = chats
                Log.d("HomeViewModel", "Chats obtenidos correctamente")
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error al obtener los chats", e)
            }
    }

    fun getMessages(token: String, chatIds: List<String>, offset: Int, limit: Int){
        viewModelScope.launch {
            async {
                getMessagesList(token, chatIds, offset, limit)
            }.await()
        }
    }

    private suspend fun getMessagesList(token: String, chatIds: List<String>, offset: Int, limit: Int) {
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


}



