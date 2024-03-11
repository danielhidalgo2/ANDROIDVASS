package com.chat.whatsvass.ui.theme.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chat.whatsvass.data.domain.model.LoginResponse
import com.chat.whatsvass.data.domain.repository.remote.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    sealed class LoginResult {
        data class Success(val login: LoginResponse) : LoginResult()
        data class Error(val message: String) : LoginResult()
    }

    private val userRepository = UserRepository()

    private val _loginResult = MutableStateFlow<LoginResult?>(null)
    val loginResult: StateFlow<LoginResult?> = _loginResult

    // Función para iniciar sesión
    fun loginUser(username: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val login = userRepository.loginUser(username, password)
                if (login.user.id.isNotEmpty()) {
                    _loginResult.value = LoginResult.Success(login)
                } else {
                    _loginResult.value = LoginResult.Error("Usuario o contraseña incorrectos")
                }
            } catch (e: Exception) {
                _loginResult.value = LoginResult.Error("Error al iniciar sesión: ${e.message}")
            }
        }
    }
}




