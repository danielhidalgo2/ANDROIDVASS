package com.chat.whatsvass.data.domain.repository.remote.mapper

import com.chat.whatsvass.data.domain.model.Login
import com.chat.whatsvass.data.domain.model.User
import com.chat.whatsvass.data.domain.repository.remote.response.login.LoginRequest

object LoginMapper {

    fun mapFromRequest(request: LoginRequest): Map<String, Any?> {
        return mapOf(
            "password" to request.password,
            "login" to request.login,
            "platform" to request.platform,
            "firebaseToken" to request.firebaseToken
        )
    }

    fun mapToLogin(response: Map<String, Any>): Login {
        val token = response["token"] as String
        val userMap = response["user"] as Map<String, Any>
        val id = userMap["id"] as String
        val nick = userMap["nick"] as String
        val avatarUrl = userMap["avatar"] as String?
        val online = userMap["online"] as Boolean
        val user = User(id, nick, avatarUrl, online)
        return Login(token, user)
    }
}
