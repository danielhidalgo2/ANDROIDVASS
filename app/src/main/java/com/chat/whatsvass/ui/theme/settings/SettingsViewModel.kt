package com.chat.whatsvass.ui.theme.settings


import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chat.whatsvass.R
import com.chat.whatsvass.data.domain.repository.remote.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class SettingsViewModel : ViewModel() {

    private val userRepository = UserRepository()

    private val _logoutResult = MutableStateFlow<String?>(null)
    val logoutResult: StateFlow<String?> = _logoutResult

    fun logoutUser(token: String, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val logout = userRepository.logoutUser(token)
                Log.d("Mensaje de respuesta", logout.message)
                _logoutResult.value = logout.message
            } catch (e: Exception) {
                Toast.makeText(context, R.string.failedToLogout, Toast.LENGTH_SHORT).show()
            }
        }
    }
}