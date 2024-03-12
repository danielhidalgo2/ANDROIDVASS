package com.chat.whatsvass.data.domain.repository.remote.mapper

import com.chat.whatsvass.data.domain.model.Login
import com.chat.whatsvass.data.domain.model.logout.Logout
import com.chat.whatsvass.data.domain.model.register.Register
import com.chat.whatsvass.data.domain.repository.remote.response.logout.LogoutResponse
import com.chat.whatsvass.data.domain.repository.remote.response.register.RegisterRequest

class LogoutMapper {
    fun mapResponse(response: LogoutResponse): Logout {
        return Logout(response.message)
    }
}
