package com.chat.whatsvass.ui.theme.profile

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.chat.whatsvass.R
import com.chat.whatsvass.commons.KEY_BIOMETRIC
import com.chat.whatsvass.commons.KEY_ID
import com.chat.whatsvass.commons.KEY_NICK
import com.chat.whatsvass.commons.KEY_TOKEN
import com.chat.whatsvass.commons.SHARED_SETTINGS
import com.chat.whatsvass.commons.SHARED_USER_DATA
import com.chat.whatsvass.commons.SOURCE_ID
import com.chat.whatsvass.data.domain.model.register.Register
import com.chat.whatsvass.data.domain.repository.remote.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private var sharedPreferences: SharedPreferences =
        application.getSharedPreferences(SHARED_USER_DATA, Context.MODE_PRIVATE)
    private var sharedPreferencesSettings: SharedPreferences =
        application.getSharedPreferences(SHARED_SETTINGS, Context.MODE_PRIVATE)

    sealed class RegisterResult {
        data class Success(val register: Register) : RegisterResult()
        data class Error(val message: String) : RegisterResult()
    }

    private val userRepository = UserRepository()

    private val _registerResult = MutableStateFlow<RegisterResult?>(null)
    val registerResult: StateFlow<RegisterResult?> = _registerResult

    fun registerUser(username: String, password: String, nick: String) {
        viewModelScope.launch(Dispatchers.IO) {

            try {
                val register = userRepository.registerUser(username, password, nick)
                if (register.user.token.isNotEmpty()) {
                    _registerResult.value = RegisterResult.Success(register)

                    sharedPreferences.edit().putString(KEY_TOKEN, register.user.token).apply()
                    sharedPreferences.edit().putString(KEY_ID, register.user.id).apply()
                    sharedPreferences.edit().putString(KEY_NICK, register.user.nick).apply()
                    sharedPreferences.edit().putString(SOURCE_ID, register.user.id).apply()

                    sharedPreferencesSettings.edit().putBoolean(KEY_BIOMETRIC, false).apply()
                } else {
                    _registerResult.value = RegisterResult.Error(R.string.failedToCreateUser.toString())
                }
            } catch (e: Exception) {
                _registerResult.value = RegisterResult.Error("${R.string.failedToCreateUser} ${e.message}")
            }

        }
    }


}




