package com.chat.whatsvass.ui.theme.profile

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.chat.whatsvass.data.constants.KEY_BIOMETRIC
import com.chat.whatsvass.data.constants.KEY_ID
import com.chat.whatsvass.data.constants.KEY_NICK
import com.chat.whatsvass.data.constants.SHARED_SETTINGS
import com.chat.whatsvass.data.constants.SHARED_USER_DATA
import com.chat.whatsvass.data.constants.SOURCE_ID
import com.chat.whatsvass.data.domain.repository.remote.UserRepository
import com.chat.whatsvass.usecases.token.Token
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private var sharedPreferences: SharedPreferences =
        application.getSharedPreferences(SHARED_USER_DATA, Context.MODE_PRIVATE)
    private var sharedPreferencesSettings: SharedPreferences =
        application.getSharedPreferences(SHARED_SETTINGS, Context.MODE_PRIVATE)

    private val userRepository = UserRepository()

    private val _registerResult = MutableStateFlow<String?>(null)
    val registerResult: StateFlow<String?> = _registerResult

    fun registerUser(username: String, password: String, nick: String) {
        viewModelScope.launch {
            async {
                registerUserPrivate(username, password, nick)
            }.await()
        }
    }

    private suspend fun registerUserPrivate(username: String, password: String, nick: String) {
        try {
            val register = userRepository.registerUser(username, password, nick)
            if (register.user.token.isNotEmpty()) {
                _registerResult.value = "success"

                sharedPreferences.edit().putString(KEY_ID, register.user.id).apply()
                sharedPreferences.edit().putString(KEY_NICK, register.user.nick).apply()
                sharedPreferences.edit().putString(SOURCE_ID, register.user.id).apply()

                sharedPreferencesSettings.edit().putBoolean(KEY_BIOMETRIC, false).apply()

                Token.token = register.user.token
            }
        } catch (e: Exception) {
            _registerResult.value = "failed"
        }
    }

}



