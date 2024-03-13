package com.chat.whatsvass.ui.theme.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chat.whatsvass.data.domain.repository.remote.ChatRepository
import com.chat.whatsvass.ui.theme.login.LoginViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
//    private val chatRepository: ChatRepository,
//    private val loginViewModel: LoginViewModel
//) : ViewModel() {
//    private val _nombres = MutableStateFlow<List<String>>(emptyList())
//    val nombres: StateFlow<List<String>> = _nombres
//
//    init {
//        obtenerChats()
//    }
//
//    private fun obtenerChats() {
//        viewModelScope.launch {
//            loginViewModel.token.collect { token ->
//                if (token.isNotEmpty()) {
//                    try {
//                        val chats = chatRepository.getChats(token)
//                        val nombres = chats.map { chat -> chat.sourceNick }
//                        _nombres.value = nombres
//                    } catch (e: Exception) {
//                        // Manejar el error
//                    }
//                }
//            }
//        }
//    }
//}

)