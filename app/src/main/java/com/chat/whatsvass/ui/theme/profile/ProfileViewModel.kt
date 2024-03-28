package com.chat.whatsvass.ui.theme.profile

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.chat.whatsvass.R
import com.chat.whatsvass.commons.KEY_BIOMETRIC
import com.chat.whatsvass.commons.KEY_ID
import com.chat.whatsvass.commons.KEY_NICK
import com.chat.whatsvass.commons.KEY_PASSWORD
import com.chat.whatsvass.commons.KEY_TOKEN
import com.chat.whatsvass.commons.SHARED_SETTINGS
import com.chat.whatsvass.commons.SHARED_USER_DATA
import com.chat.whatsvass.commons.SOURCE_ID
import com.chat.whatsvass.data.domain.model.register.Register
import com.chat.whatsvass.data.domain.repository.remote.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
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

    private val isFunCalled = MutableStateFlow(true)
    var isFunCalledFlow: Flow<Boolean> = isFunCalled

    fun registerUser(username: String, password: String, nick: String) {
        viewModelScope.launch{
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

                    sharedPreferences.edit().putString(KEY_TOKEN, register.user.token).apply()
                    sharedPreferences.edit().putString(KEY_ID, register.user.id).apply()
                    sharedPreferences.edit().putString(KEY_NICK, register.user.nick).apply()
                    sharedPreferences.edit().putString(SOURCE_ID, register.user.id).apply()

                    sharedPreferencesSettings.edit().putBoolean(KEY_BIOMETRIC, false).apply()
                }
            } catch (e: Exception) {
                _registerResult.value =  "failed"
            }
        }

}



