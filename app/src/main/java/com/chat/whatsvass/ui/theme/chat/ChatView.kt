package com.chat.whatsvass.ui.theme.chat

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chat.whatsvass.R
import com.chat.whatsvass.commons.KEY_TOKEN
import com.chat.whatsvass.commons.SHARED_USER_DATA
import com.chat.whatsvass.commons.SOURCE_ID
import com.chat.whatsvass.data.domain.model.message.Message
import com.chat.whatsvass.data.domain.repository.remote.response.create_message.MessageRequest
import com.chat.whatsvass.ui.theme.Oscuro
import com.chat.whatsvass.ui.theme.Principal
import com.chat.whatsvass.ui.theme.White
import com.chat.whatsvass.ui.theme.home.formatTimeFromApi
import com.chat.whatsvass.ui.theme.login.hideKeyboard

private lateinit var sharedPreferencesToken: SharedPreferences

class ChatView : ComponentActivity() {
    private val viewModel: ChatViewModel by viewModels()


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferencesToken = getSharedPreferences(SHARED_USER_DATA, Context.MODE_PRIVATE)
        val token = sharedPreferencesToken.getString(KEY_TOKEN, null)

         val chatId = intent.getStringExtra("ChatID")
         val nick = intent.getStringExtra("Nick")

        setContent {
            val messages by viewModel.message.collectAsState(emptyMap())

            // Llamar a la función getMessages después de obtener los chats

            if (token != null && chatId != null) {
                viewModel.getMessagesForChat(token, chatId, offset = 0, limit = 100)
            }


            if (nick != null) {
                ChatScreen(chatId = chatId, messages = messages, nick = nick)
            }

        }
        window.decorView.setOnTouchListener { _, _ ->
            hideKeyboard(this)
            false
        }
    }


    @Composable
    fun ChatScreen(chatId: String?, messages: Map<String, List<Message>>, nick: String) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                TopBarChat(nick)
                chatId?.let { MessageList(chatId = it, messages = messages) }
            }
            BottomBar(chatId,onSendMessage = { /* Acción al enviar el mensaje */ })
        }
    }


    @Composable
    fun TopBarChat(nick: String) {

        TopAppBar(
            backgroundColor = Principal,
            elevation = 4.dp,
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(vertical = 12.dp, horizontal = 16.dp)
                    .fillMaxWidth()
                    .clickable { }
                    .requiredWidth(width = 368.dp)
                    .requiredHeight(height = 74.dp)
                    .clip(shape = RoundedCornerShape(20.dp))
                    .background(Principal),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    finish()
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_arrow_back),
                        contentDescription = "Back"
                    )
                }
                Spacer(modifier = Modifier.weight(0.1f))
                Box(
                    modifier = Modifier
                        .size(53.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(Color.LightGray),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.image_person),
                            contentDescription = "Foto de perfil",
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(4.dp)
                        )
                    }
                    Icon(
                        painter = painterResource(id = R.drawable.ic_circle),
                        contentDescription = "Custom Icon",
                        tint = Color.Green, // Comprobar si esta online / offline
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(15.dp)
                    )
                }

                Text(
                    text = nick,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .weight(1f)
                )

            }
        }
    }

    @Composable
    fun MessageList(chatId: String, messages: Map<String, List<Message>>) {
        val sourceId = sharedPreferencesToken.getString(SOURCE_ID, null)

        val chatMessages = messages[chatId] ?: emptyList()
        LazyColumn {
            items(chatMessages) { message ->
                if (message.source == sourceId) {
                    MessageItem(message, true)

                } else {
                    MessageItem(message, false)

                }
            }
        }
    }


    @Composable
    fun MessageItem(messages: Message, isSentByUser: Boolean) {
        val horizontalPadding = 30.dp
        val verticalPadding = 8.dp


        val backgroundColor = White
        val alignment = if (isSentByUser) TextAlign.Start else TextAlign.End
        val startPadding = if (isSentByUser) horizontalPadding else 0.dp
        val endPadding = if (isSentByUser) 0.dp else horizontalPadding

        val formattedTime = formatTimeFromApi(messages.date) ?: "N/A"


        Row(
            modifier = Modifier
                .padding(vertical = verticalPadding, horizontal = horizontalPadding)
                .fillMaxWidth(),
            horizontalArrangement = if (isSentByUser) Arrangement.End else Arrangement.Start,
        ) {

            Box(
                modifier = Modifier
                    .padding(start = startPadding, end = endPadding)
                    .background(
                        color = backgroundColor,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(vertical = verticalPadding, horizontal = horizontalPadding)
            ) {

                Column(modifier = Modifier.align(Alignment.BottomEnd)) {
                    Text(
                        text = messages.message ?: "No hay mensajes",
                        color = Color.Black,
                        textAlign = alignment,
                    )
                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = formattedTime,
                        style = TextStyle(fontSize = 14.sp, color = Color.Gray),

                        )
                }
            }


        }
    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun BottomBar(chatId:String?,onSendMessage: (String) -> Unit) {
        var messageText by remember { mutableStateOf("") }
        val token = sharedPreferencesToken.getString(KEY_TOKEN, null)
        val sourceID = sharedPreferencesToken.getString(SOURCE_ID, null)


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            TextField(
                value = messageText,
                onValueChange = { messageText = it },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
                    .background(Color.Transparent)
                    .clip(RoundedCornerShape(40.dp)),
                placeholder = { Text(text = "Escribe un mensaje...") },
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent, // Ocultar la línea de foco
                    unfocusedIndicatorColor = Color.Transparent // Ocultar la línea de enfoque
                )
            )
            IconButton(
                onClick = {
                    if (token != null) {
                       viewModel.createNewMessage(token, MessageRequest(chat = chatId!!, source = sourceID!! ,messageText))
                    }
                    onSendMessage(messageText)
                    messageText = ""
                }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Enviar",
                    Modifier.size(40.dp),
                    tint = Oscuro
                )
            }
        }
    }
}