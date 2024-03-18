package com.chat.whatsvass.ui.theme.chat

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chat.whatsvass.data.domain.model.chat.Chat
import com.chat.whatsvass.data.domain.model.create_chat.CreatedChat
import com.chat.whatsvass.data.domain.model.create_message.CreateMessage
import com.chat.whatsvass.data.domain.model.message.Message
import com.chat.whatsvass.data.domain.repository.remote.ChatRepository
import com.chat.whatsvass.data.domain.repository.remote.response.create_chat.ChatRequest
import com.chat.whatsvass.data.domain.repository.remote.response.create_message.MessageRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChatViewModel: ViewModel() {
    private val chatRepository = ChatRepository()

    private val _messages = MutableStateFlow<Map<String, List<Message>>>(emptyMap())
    val message: StateFlow<Map<String, List<Message>>> = _messages

    fun getMessagesForChat(token: String, chatId: String, offset: Int, limit: Int) {
        viewModelScope.launch {
            try {
                val messages = chatRepository.getMessages(token, chatId, offset, limit)
                val messagesMap = mutableMapOf<String, List<Message>>()
                messagesMap[chatId] = messages
                _messages.value = messagesMap
                Log.d("HomeViewModel", "Mensajes obtenidos correctamente para el chat con ID: $chatId")
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error al obtener los mensajes para el chat con ID: $chatId", e)
            }
        }
    }

    private val _newMessageResult = MutableStateFlow<CreateMessage?>(null)
    val newChatResult: StateFlow<CreateMessage?> = _newMessageResult
    fun createNewMessage(token: String, messageRequest: MessageRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val newMessage = chatRepository.createNewMessage(token, messageRequest)
                _newMessageResult.value = newMessage
                Log.d("Nuevo chat", newMessage.toString())

            } catch (e: Exception) {
                Log.d("Nuevo chat", "Error al crear mensaje: ${e.message}")
            }
        }
    }

}
