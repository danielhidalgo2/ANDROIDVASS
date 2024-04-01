package com.chat.whatsvass.ui.theme.login

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.CheckBox
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.chat.whatsvass.R
import com.chat.whatsvass.commons.KEY_ID
import com.chat.whatsvass.commons.KEY_NICK
import com.chat.whatsvass.commons.KEY_TOKEN
import com.chat.whatsvass.commons.SOURCE_ID
import com.chat.whatsvass.commons.SHARED_USER_DATA
import com.chat.whatsvass.data.domain.repository.remote.UserRepository
import com.chat.whatsvass.data.domain.repository.remote.response.login.LoginResponse
import com.chat.whatsvass.usecases.encrypt.Encrypt
import com.chat.whatsvass.usecases.token.Token
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private var sharedPreferences: SharedPreferences =
        application.getSharedPreferences(SHARED_USER_DATA, Context.MODE_PRIVATE)

    sealed class LoginResult {
        data class Success(val login: LoginResponse) : LoginResult()
        data class Error(val message: String) : LoginResult()
    }

    private val userRepository = UserRepository()

    val _loginResult = MutableStateFlow<LoginResult?>(null)

    fun loginUser(username: String, password: String) {
        if (username.isEmpty() || password.isEmpty()) {
            _loginResult.value = LoginResult.Error(R.string.pleaseEnterUserAndPassword.toString())
            return
        }
        val encryptedPassword = Encrypt().encryptPassword(password)

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val login = userRepository.loginUser(username, encryptedPassword)
                if (login.token.isNotEmpty()) {
                    _loginResult.value = LoginResult.Success(login)
                    Log.d("LoginViewModel", "Inicio de sesión exitoso. Token: ${login.token}")
                    sharedPreferences.edit().putString(KEY_TOKEN, login.token).apply()
                    sharedPreferences.edit().putString(KEY_ID, login.user.id).apply()
                    sharedPreferences.edit().putString(KEY_NICK, login.user.nick).apply()
                    sharedPreferences.edit().putString(SOURCE_ID, login.user.id).apply()

                    Token.token = login.token

                } else {
                    _loginResult.value = LoginResult.Error(R.string.incorrectUserOrPassword.toString())
                    Log.d(
                        "LoginViewModel",
                        "Inicio de sesión fallido. Usuario o contraseña incorrectos"
                    )
                }
            } catch (e: Exception) {
                _loginResult.value = LoginResult.Error("${R.string.failedToLogin} ${e.message}")
                Log.e("LoginViewModel", "Error al iniciar sesión: ${e.message}")
            }
        }
    }




}






