package com.chat.whatsvass.ui.theme.login

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.chat.whatsvass.commons.KEY_TOKEN
import com.chat.whatsvass.commons.SHARED_TOKEN
import com.chat.whatsvass.commons.SOURCE_ID
import com.chat.whatsvass.data.domain.repository.remote.UserRepository
import com.chat.whatsvass.data.domain.repository.remote.response.login.LoginResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private var sharedPreferences: SharedPreferences =
        application.getSharedPreferences(SHARED_TOKEN, Context.MODE_PRIVATE)
    val edit = sharedPreferences.edit()

    sealed class LoginResult {
        data class Success(val login: LoginResponse) : LoginResult()
        data class Error(val message: String) : LoginResult()
    }

    private val userRepository = UserRepository()

    val _loginResult = MutableStateFlow<LoginResult?>(null)

    fun loginUser(username: String, password: String) {
        if (username.isEmpty() || password.isEmpty()) {
            _loginResult.value = LoginResult.Error("Por favor, ingrese su usuario y contraseña")
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val login = userRepository.loginUser(username, password)
                if (login.token.isNotEmpty()) {
                    _loginResult.value = LoginResult.Success(login)
                    Log.d("LoginViewModel", "Inicio de sesión exitoso. Token: ${login.token}")
                    edit.putString(KEY_TOKEN, login.token)
                    edit.putString(SOURCE_ID, login.user.id)
                    edit.commit()
                } else {
                    _loginResult.value = LoginResult.Error("Usuario o contraseña incorrectos")
                    Log.d(
                        "LoginViewModel",
                        "Inicio de sesión fallido. Usuario o contraseña incorrectos"
                    )
                }
            } catch (e: Exception) {
                _loginResult.value = LoginResult.Error("Error al iniciar sesión: ${e.message}")
                Log.e("LoginViewModel", "Error al iniciar sesión: ${e.message}")
            }
        }
    }


}






