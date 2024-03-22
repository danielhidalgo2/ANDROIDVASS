package com.chat.whatsvass.ui.theme.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chat.whatsvass.data.domain.model.register.Register
import com.chat.whatsvass.data.domain.repository.remote.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    sealed class RegisterResult {
        data class Success(val register: Register) : RegisterResult()
        data class Error(val message: String) : RegisterResult()
    }

    private val userRepository = UserRepository()

    private val _registerResult = MutableStateFlow<RegisterResult?>(null)
    val registerResult: StateFlow<RegisterResult?> = _registerResult

    private val _registerData = MutableStateFlow<Register?>(null)
    val registerData: StateFlow<Register?> = _registerData

    fun registerUser(username: String, password: String, nick: String) {
        viewModelScope.launch(Dispatchers.IO) {

            try {
                val register = userRepository.registerUser(username, password, nick)
                if (register.user.token.isNotEmpty()) {
                    _registerResult.value = RegisterResult.Success(register)
                    _registerData.value = register

                } else {
                    _registerResult.value = RegisterResult.Error("Error al crear usuario")
                }
            } catch (e: Exception) {
                _registerResult.value = RegisterResult.Error("Error al crear usuario: ${e.message}")
            }

        }
    }
}




