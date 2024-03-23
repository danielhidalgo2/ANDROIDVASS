package com.chat.whatsvass.ui.theme.settings


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chat.whatsvass.R
import com.chat.whatsvass.data.domain.repository.remote.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class SettingsViewModel : ViewModel() {

    sealed class LogoutResult {
        data class Success(val logout: String) : LogoutResult()
        data class Error(val message: String) : LogoutResult()
    }

    private val userRepository = UserRepository()

    private val _logoutResult = MutableStateFlow<LogoutResult?>(null)
    val logoutResult: StateFlow<LogoutResult?> = _logoutResult

    fun logoutUser(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val logout = userRepository.logoutUser(token)
                Log.d("Mensaje de respuesta", logout.message)
                if (logout.message == "Logout successful") {
                    _logoutResult.value = LogoutResult.Success(logout.message)
                } else {
                    _logoutResult.value = LogoutResult.Error(R.string.failedToLogout.toString())
                }
            } catch (e: Exception) {
                _logoutResult.value = LogoutResult.Error("${R.string.failedToLogout}: ${e.message}")
            }
        }
    }
}