package com.chat.whatsvass.ui.theme.login

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.chat.whatsvass.R
import com.chat.whatsvass.commons.CHECK_BOX
import com.chat.whatsvass.commons.KEY_ID
import com.chat.whatsvass.commons.KEY_NICK
import com.chat.whatsvass.commons.KEY_TOKEN
import com.chat.whatsvass.commons.SHARED_USER_DATA
import com.chat.whatsvass.commons.SOURCE_ID
import com.chat.whatsvass.data.domain.repository.remote.UserRepository
import com.chat.whatsvass.data.domain.repository.remote.response.login.LoginResponse
import com.chat.whatsvass.usecases.encrypt.Encrypt
import com.chat.whatsvass.usecases.token.Token
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private var sharedPreferences: SharedPreferences =
        application.getSharedPreferences(SHARED_USER_DATA, Context.MODE_PRIVATE)

    sealed class LoginResult {
        data class Success(val login: LoginResponse) : LoginResult()
        data class Error(val message: String) : LoginResult()
    }

    private val userRepository = UserRepository()

    private val _loginResult = MutableStateFlow<LoginResult?>(null)
    val loginResult: StateFlow<LoginResult?> = _loginResult

    fun loginUser(username: String, password: String, checkBox: Boolean? = null) {
        if (username.isEmpty() || password.isEmpty()) {
            _loginResult.value = LoginResult.Error(R.string.pleaseEnterUserAndPassword.toString())
            return
        }
        val encryptedPassword = Encrypt().encryptPassword(password)

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val login = userRepository.loginUser(username, encryptedPassword)
                if (login.token.isNotEmpty()) {
                    if (checkBox != null) {
                        sharedPreferences.edit().putBoolean(CHECK_BOX, checkBox).apply()
                    }
                    _loginResult.value = LoginResult.Success(login)
                    sharedPreferences.edit().putString(KEY_TOKEN, login.token).apply()
                    sharedPreferences.edit().putString(KEY_ID, login.user.id).apply()
                    sharedPreferences.edit().putString(KEY_NICK, login.user.nick).apply()
                    sharedPreferences.edit().putString(SOURCE_ID, login.user.id).apply()

                    Token.token = login.token

                } else {
                    _loginResult.value =
                        LoginResult.Error(R.string.incorrectUserOrPassword.toString())
                }
            } catch (e: Exception) {
                _loginResult.value = LoginResult.Error("${R.string.failedToLogin} ${e.message}")
                _loginResult.value = null
            }
        }
    }

}






