package com.chat.whatsvass.ui.theme.contacts

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.chat.whatsvass.R
import com.chat.whatsvass.commons.KEY_NICK
import com.chat.whatsvass.commons.SHARED_USER_DATA
import com.chat.whatsvass.data.domain.model.contacts.Contacts
import com.chat.whatsvass.data.domain.model.create_chat.CreatedChat
import com.chat.whatsvass.data.domain.repository.remote.ContactsRepository
import com.chat.whatsvass.data.domain.repository.remote.response.create_chat.ChatRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ContactsViewModel(application: Application) : AndroidViewModel(application) {

    private var sharedPreferences: SharedPreferences =
        application.getSharedPreferences(SHARED_USER_DATA, Context.MODE_PRIVATE)
    private val myNick = sharedPreferences.getString(KEY_NICK, null)

    private val contactsRepository = ContactsRepository()

    private val isTextWithOutContactsVisible = MutableStateFlow(false)
    var isTextWithOutVisibleFlow: Flow<Boolean> = isTextWithOutContactsVisible

    private val isProgressBarVisible = MutableStateFlow(true)
    var isProgressVisibleFlow: Flow<Boolean> = isProgressBarVisible

    private val _contactsResult = MutableStateFlow<List<Contacts>>(emptyList())
    val contactsResult: StateFlow<List<Contacts>> = _contactsResult

    fun getContacts(token: String) {
        viewModelScope.launch {
            async {
                getContactsList(token)
                if (_contactsResult.value.isNotEmpty()) {
                    for (i in _contactsResult.value) {
                        if (myNick != null) {
                            // Quitar mi usuario de la lista de contactos
                            val listOfContacts = _contactsResult.value.filter { it.nick != myNick }
                            _contactsResult.value = listOfContacts
                        }
                    }
                }
            }.await()
            if (contactsResult.value.isEmpty()) {
                isTextWithOutContactsVisible.value = true
            }
            isProgressBarVisible.value = false
        }
    }

    private suspend fun getContactsList(token: String) {
        try {
            val contacts = contactsRepository.getContacts(token)
            _contactsResult.value = contacts
            isTextWithOutContactsVisible.value = false
        } catch (e: Exception) {
            _contactsResult.value = emptyList()
        }
    }

    private val _newChatResult = MutableStateFlow<CreatedChat?>(null)
    val newChatResult: StateFlow<CreatedChat?> = _newChatResult

    private val isNewChatCreated = MutableStateFlow(false)
    var isNewChatCreatedFlow: Flow<Boolean> = isNewChatCreated
    fun createNewChat(token: String, chatRequest: ChatRequest) {
        isNewChatCreated.value = false
        viewModelScope.launch(Dispatchers.IO) {
            async {
                createNewChatModel(token, chatRequest)
            }.await()
            isNewChatCreated.value = true
        }
    }

    private suspend fun createNewChatModel(
        token: String,
        chatRequest: ChatRequest
    ) {
        try {
            val newChat = contactsRepository.createNewChat(token, chatRequest)
            _newChatResult.value = newChat

        } catch (e: Exception) {
           _newChatResult.value = null
        }
    }
}






