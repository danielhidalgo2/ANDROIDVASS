package com.chat.whatsvass.ui.theme.contacts
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chat.whatsvass.commons.KEY_TOKEN
import com.chat.whatsvass.commons.SHARED_TOKEN
import com.chat.whatsvass.data.domain.model.contacts.Contacts
import com.chat.whatsvass.data.domain.repository.remote.ContactsRepository
import com.chat.whatsvass.ui.theme.home.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ContactsViewModel: ViewModel() {

    private val contactsRepository = ContactsRepository()

    private val _contactsResult = MutableStateFlow<List<Contacts>>(emptyList())
    val contactsResult: StateFlow<List<Contacts>> = _contactsResult
    fun getContacts(token: String) {
        viewModelScope.launch(Dispatchers.IO) {

            try {

                val contacts = contactsRepository.getContacts(token!!)
                _contactsResult.value = contacts
                Log.d("Contactos", contacts.toString())
            } catch (e: Exception) {
                Log.d("Contactos", "$token")
                Log.d("Contactos", "Error al mostrar contactos: ${e.message}")
            }
        }
    }
}






