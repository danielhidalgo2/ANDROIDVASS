package com.chat.whatsvass.data.domain.repository.remote

import android.util.Log
import com.chat.whatsvass.data.domain.model.logout.Logout
import com.chat.whatsvass.data.domain.model.register.Register
import com.chat.whatsvass.data.domain.repository.remote.response.login.LoginResponse
import com.chat.whatsvass.data.domain.repository.remote.mapper.LoginMapper
import com.chat.whatsvass.data.domain.repository.remote.mapper.LogoutMapper
import com.chat.whatsvass.data.domain.repository.remote.mapper.RegisterMapper
import com.chat.whatsvass.data.domain.repository.remote.response.ApiService
import com.chat.whatsvass.data.domain.repository.remote.response.RetrofitClient
import com.chat.whatsvass.usecases.encrypt.Encrypt

class UserRepository {
    private val apiService: ApiService = RetrofitClient.apiService

    suspend fun loginUser(username: String, password: String): LoginResponse {
        Log.d("Encrypt", "Encriptada $password")

        val desencryptedPassword = Encrypt().decryptPassword(password)
        Log.d("Encrypt", "Desencriptada $desencryptedPassword")

        val request = LoginMapper().mapRequest(username, desencryptedPassword)
        return apiService.loginUser(request)
    }
    suspend fun registerUser(username: String, password: String, nick:String): Register {
        val request = RegisterMapper().mapRequest(username, password, nick)
        return apiService.registerUser(request)
    }
    suspend fun logoutUser(token: String): Logout {
        val request = apiService.logoutUser(token)
        return LogoutMapper().mapResponse(request)
    }

    suspend fun updateUserOnlineStatus(token: String, isOnline: Boolean): Boolean {
        return try {
            val status = if (isOnline) "true" else "false"
            val response = apiService.updateUserOnlineStatus(status, token)
            response.isSuccessful
        } catch (e: Exception) {
            // Manejar errores aquí
            false
        }
    }
}

