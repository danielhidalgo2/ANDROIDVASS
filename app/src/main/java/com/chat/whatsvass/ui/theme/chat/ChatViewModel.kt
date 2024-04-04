package com.chat.whatsvass.ui.theme.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chat.whatsvass.commons.LIMIT_GET_MESSAGESFORCHAT
import com.chat.whatsvass.commons.OFFSET_GET_MESSAGESFORCHAT
import com.chat.whatsvass.data.domain.model.message.Message
import com.chat.whatsvass.data.domain.repository.remote.ChatRepository
import com.chat.whatsvass.data.domain.repository.remote.response.create_message.MessageRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {
    private val chatRepository = ChatRepository()

    private val _messages = MutableStateFlow<Map<String, List<Message>>>(emptyMap())
    val message: StateFlow<Map<String, List<Message>>> = _messages

    private val isTextStartTheChatVisible = MutableStateFlow(false)
    var isTextStartTheChatVisibleFlow: Flow<Boolean> = isTextStartTheChatVisible

    fun getMessagesForChat(token: String, chatId: String, offset: Int, limit: Int) {
        viewModelScope.launch {
            async {
                getMessagesForChatPrivate(token, chatId, offset, limit)
            }.await()
            isTextStartTheChatVisible.value = true
        }
    }

    private suspend fun getMessagesForChatPrivate(token: String, chatId: String, offset: Int, limit: Int) {
        try {
            val messages = chatRepository.getMessages(token, chatId, offset, limit)
            val messagesMap = mutableMapOf<String, List<Message>>()
            messagesMap[chatId] = messages
            _messages.value = messagesMap
        } catch (e: Exception) {
            throw e

        }

    }

    fun createNewMessageAndReload(token: String, messageRequest: MessageRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                chatRepository.createNewMessage(token, messageRequest)
                // Recarga todos los mensajes
                getMessagesForChat(
                    token, messageRequest.chat, OFFSET_GET_MESSAGESFORCHAT, LIMIT_GET_MESSAGESFORCHAT
                )
            } catch (e: Exception) {
                throw e
            }
        }
    }

}
