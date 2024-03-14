package com.chat.whatsvass.data.domain.repository.remote.mapper

import com.chat.whatsvass.data.domain.model.logout.Logout
import com.chat.whatsvass.data.domain.repository.remote.response.logout.LogoutResponse

class LogoutMapper {
    fun mapResponse(response: LogoutResponse): Logout {
        return Logout(response.message)
    }
}
