package com.chat.whatsvass.ui.theme.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chat.whatsvass.data.domain.repository.remote.UserRepository
import com.chat.whatsvass.data.domain.repository.remote.response.login.LoginRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val userRepository = UserRepository()

    // Función para iniciar sesión
    fun loginUser(username: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val loginRequest = LoginRequest(username, password)
                val login = userRepository.loginUser(loginRequest)

                // Si el inicio de sesión es correcto, imprime un mensaje de éxito en la consola
                println("Inicio de sesión exitoso. Token: ${login.token}")

                // Aquí podrías hacer algo más, como navegar a la siguiente pantalla o mostrar un mensaje de éxito al usuario.

            } catch (e: Exception) {
                // Si ocurre un error durante el inicio de sesión, imprime un mensaje de error en la consola
                println("Error al iniciar sesión: ${e.message}")

                // Manejar errores, como mostrar un mensaje de error al usuario
                // en caso de credenciales inválidas o problemas de conexión.
            }
        }
    }
}
