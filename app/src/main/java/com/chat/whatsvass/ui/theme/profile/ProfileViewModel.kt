package com.chat.whatsvass.ui.theme.profile

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
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

    private val _registerResult = MutableStateFlow<Register?>(null)
    val registerResult: StateFlow<Register?> = _registerResult


    fun registerUser(username: String, password: String, nick: String, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val register = userRepository.registerUser(username, password, nick)
                if (register.user.token.isNotEmpty()) {
                    sharedPreferences.edit().putString(KEY_TOKEN, register.user.token).apply()
                    sharedPreferences.edit().putString(KEY_ID, register.user.id).apply()
                    sharedPreferences.edit().putString(KEY_NICK, register.user.nick).apply()
                    sharedPreferences.edit().putString(SOURCE_ID, register.user.id).apply()

                    sharedPreferencesSettings.edit().putBoolean(KEY_BIOMETRIC, false).apply()
                }
                _registerResult.value = register

            } catch (e: Exception) {
                Toast.makeText(ProfileView(), R.string.failedToCreateUser, Toast.LENGTH_SHORT).show()
                Log.d("REGISTER", "${R.string.failedToCreateUser} ${e.message}")
            }
        }

    }
}




