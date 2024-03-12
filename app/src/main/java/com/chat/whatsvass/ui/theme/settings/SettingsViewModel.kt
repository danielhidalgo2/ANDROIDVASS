package com.chat.whatsvass.ui.theme.settings


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chat.whatsvass.data.domain.model.logout.Logout
import com.chat.whatsvass.data.domain.repository.remote.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class SettingsViewModel : ViewModel() {

    sealed class LogoutResult {
        data class Success(val logout: Logout) : LogoutResult()
        data class Error(val message: String) : LogoutResult()
    }

    private val userRepository = UserRepository()

    private val _logoutResult = MutableStateFlow<LogoutResult?>(null)
    val logoutResult: StateFlow<LogoutResult?> = _logoutResult

    fun logoutUser(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val logout = userRepository.logoutUser(token)
                if (logout.message == "Logout successful") {
                    _logoutResult.value = LogoutResult.Success(logout)
                } else {
                    _logoutResult.value = LogoutResult.Error("Error al cerrar sesión")
                }
            } catch (e: Exception) {
                _logoutResult.value = LogoutResult.Error("Error al cerrar sesión: ${e.message}")
            }
        }
    }
}