package com.chat.whatsvass.ui.theme.chat

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chat.whatsvass.R
import com.chat.whatsvass.commons.KEY_TOKEN
import com.chat.whatsvass.commons.SHARED_TOKEN
import com.chat.whatsvass.data.domain.model.chat.Chat
import com.chat.whatsvass.data.domain.model.message.Message
import com.chat.whatsvass.ui.theme.Claro
import com.chat.whatsvass.ui.theme.Contraste
import com.chat.whatsvass.ui.theme.Oscuro
import com.chat.whatsvass.ui.theme.Principal
import com.chat.whatsvass.ui.theme.White
import com.chat.whatsvass.ui.theme.home.HomeViewModel

class ChatView : ComponentActivity() {
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var sharedPreferencesToken: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferencesToken = getSharedPreferences(SHARED_TOKEN, Context.MODE_PRIVATE)
        val token = sharedPreferencesToken.getString(KEY_TOKEN, null)

        setContent {
            LaunchedEffect(key1 = viewModel) {
                if (token != null) {
                    viewModel.getChats(token)
                    Log.d("HomeView", "Obteniendo chats con token: $token")
                }
        }
        val messages by viewModel.messages.collectAsState(emptyMap())
            val chats by viewModel.chats.collectAsState(emptyList())

        // Llamar a la función getMessages después de obtener los chats
        LaunchedEffect(key1 = chats) {
            if (token != null && chats.isNotEmpty()) {
                val chatIds = chats.map { it.chatId }
                viewModel.getMessages(token, chatIds, offset = 0, limit = 100)
            }
        }

       ChatScreen(chats = chats, messages = messages)

    }
}
}

val listaStrings = listOf(
    "Holaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
    "Cómo estás?",
    "Qué tal?",
    "Buenos días",
    "Buenas tardes",
    "Buenas noches",
    "Adiós",
    "Hasta luego",
    "Nos vemos pronto",
    "Qué tengas un buen día",
    "Feliz cumpleaños",
    "Feliz Navidad",
    "Feliz Año Nuevo"
)

@Composable
fun ChatScreen(chats: List<Chat>, messages: Map<String, List<Message>>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            TopBarChat()
            MessageList(chats = chats, messages = messages)
        }
        BottomBar(onSendMessage = { /* Acción al enviar el mensaje */ })
    }
}


@Composable
fun TopBarChat() {
    val context = LocalContext.current

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
                        .clickable { }
                        .align(Alignment.BottomEnd)
                        .size(15.dp)
                )
            }

            Text(
                text = "userName",
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
fun MessageList(chats: List<Chat>, messages: Map<String, List<Message>>) {
    LazyColumn {
        items(chats) { chat ->
            val chatMessages = messages[chat.chatId] ?: emptyList()
            MessageItem(chat = chat, messages = chatMessages, true)
        }

    }
}


@Composable
fun MessageItem(chat: Chat, messages: List<Message>, isSentByUser: Boolean) {
    val horizontalPadding = 30.dp
    val verticalPadding = 8.dp
    val lastMessage = messages.lastOrNull()


    val backgroundColor = White
    val alignment = if (isSentByUser) TextAlign.End else TextAlign.Start
    val startPadding = if (isSentByUser) horizontalPadding else 0.dp
    val endPadding = if (isSentByUser) 0.dp else horizontalPadding

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
                    text = lastMessage?.message ?: "No hay mensajes",
                    color = Color.Black,
                    textAlign = alignment,
                )
                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "22:00",
                    style = TextStyle(fontSize = 14.sp, color = Color.Gray),

                )
            }
        }



    }
}


@Composable
fun BottomBar(onSendMessage: (String) -> Unit) {
    var messageText by remember { mutableStateOf("") }

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
            singleLine = true
        )
        IconButton(
            onClick = {
                onSendMessage(messageText)
                messageText = "" // Limpiar el campo de texto después de enviar el mensaje
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