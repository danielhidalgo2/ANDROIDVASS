package com.chat.whatsvass.data.domain.repository.remote.response

import com.chat.whatsvass.data.domain.repository.remote.response.login.LoginResponse
import com.chat.whatsvass.data.domain.repository.remote.response.chat.ChatResponse
import com.chat.whatsvass.data.domain.model.login.Login
import com.chat.whatsvass.data.domain.model.register.Register
import com.chat.whatsvass.data.domain.repository.remote.response.contacts.ContactsResponse
import com.chat.whatsvass.data.domain.repository.remote.response.create_chat.ChatRequest
import com.chat.whatsvass.data.domain.repository.remote.response.create_chat.CreatedChatResponse
import com.chat.whatsvass.data.domain.repository.remote.response.logout.LogoutResponse
import com.chat.whatsvass.data.domain.repository.remote.response.message.MessagesResponse
import com.chat.whatsvass.data.domain.repository.remote.response.register.RegisterRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @POST("users/login")
    suspend fun loginUser(@Body request: Login): LoginResponse

    @POST("users/register")
    suspend fun registerUser(@Body request: RegisterRequest): Register

    @POST("users/logout")
    suspend fun logoutUser(@Header("Authorization") token: String): LogoutResponse

    @GET("chats/view")
    suspend fun getChats(@Header("Authorization") token: String): List<ChatResponse>

    @GET("messages/list/{chatId}")
    suspend fun getMessages(
        @Path("chatId") chatId: String,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
        @Header("Authorization") token: String
    ): MessagesResponse

    @GET("users")
    suspend fun getContacts(@Header("Authorization") token: String): List<ContactsResponse>

    @POST("chats")
    suspend fun createChat(
        @Header("Authorization") token: String,
        @Body request: ChatRequest
    ): CreatedChatResponse
}

