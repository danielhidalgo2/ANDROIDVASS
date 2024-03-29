package com.chat.whatsvass.ui.theme.chat

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.chat.whatsvass.R
import com.chat.whatsvass.commons.CHAT_ID_ARGUMENT
import com.chat.whatsvass.commons.DELAY_GET_MESSAGESFORCHAT
import com.chat.whatsvass.commons.KEY_MODE
import com.chat.whatsvass.commons.KEY_TOKEN
import com.chat.whatsvass.commons.KNICK_ARGUMENT
import com.chat.whatsvass.commons.LIMIT_GET_MESSAGESFORCHAT
import com.chat.whatsvass.commons.OFFSET_GET_MESSAGESFORCHAT
import com.chat.whatsvass.commons.ONLINE_ARGUMENT
import com.chat.whatsvass.commons.SHARED_SETTINGS
import com.chat.whatsvass.commons.SHARED_USER_DATA
import com.chat.whatsvass.commons.SOURCE_ID
import com.chat.whatsvass.data.domain.model.message.Message
import com.chat.whatsvass.data.domain.repository.remote.response.create_message.MessageRequest
import com.chat.whatsvass.ui.theme.Contrast
import com.chat.whatsvass.ui.theme.Dark
import com.chat.whatsvass.ui.theme.DarkMode
import com.chat.whatsvass.ui.theme.Main
import com.chat.whatsvass.ui.theme.White
import com.chat.whatsvass.ui.theme.home.HomeView
import com.chat.whatsvass.ui.theme.login.hideKeyboard
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

private lateinit var sharedPreferencesToken: SharedPreferences
private lateinit var sharedPreferencesSettings: SharedPreferences

class ChatView : ComponentActivity() {
    private val viewModel: ChatViewModel by viewModels()

    var online = false

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.decorView.apply {
            @Suppress("DEPRECATION")
            systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        }

        sharedPreferencesToken = getSharedPreferences(SHARED_USER_DATA, Context.MODE_PRIVATE)
        val token = sharedPreferencesToken.getString(KEY_TOKEN, null)

        sharedPreferencesSettings = getSharedPreferences(SHARED_SETTINGS, Context.MODE_PRIVATE)
        val isDarkModeActive = sharedPreferencesSettings.getBoolean(KEY_MODE, false)

        val chatId = intent.getStringExtra(CHAT_ID_ARGUMENT)
        val nick = intent.getStringExtra(KNICK_ARGUMENT)

        setContent {
            val messages by viewModel.message.collectAsState(emptyMap())

            if (token != null && chatId != null) {
                viewModel.getMessagesForChat(token, chatId, OFFSET_GET_MESSAGESFORCHAT, LIMIT_GET_MESSAGESFORCHAT)
            }


            if (nick != null) {
                ChatScreen(chatId = chatId, messages = messages, nick = nick, isDarkModeActive)
            }


        }
        window.decorView.setOnTouchListener { _, _ ->
            hideKeyboard(this)
            false
        }
    }


    @Composable
    fun ChatScreen(
        chatId: String?,
        messages: Map<String, List<Message>>,
        nick: String,
        isDarkModeActive: Boolean
    ) {
        val token = sharedPreferencesToken.getString(KEY_TOKEN, null)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(if (isDarkModeActive) DarkMode else Color.White)
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                TopBarChat(nick)
                chatId?.let { MessageList(chatId = it, messages = messages, token!!, isDarkModeActive) }
            }
            BottomBar(chatId, isDarkModeActive, onSendMessage = { /* Acción al enviar el mensaje */ })
        }
    }


    @Composable
    fun TopBarChat(nick: String) {
        online = intent.getStringExtra(ONLINE_ARGUMENT).toBoolean()
        TopAppBar(
            backgroundColor = Main,
            elevation = 4.dp,
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(vertical = 12.dp, horizontal = 16.dp)
                    .fillMaxWidth()
                    .requiredWidth(width = 368.dp)
                    .requiredHeight(height = 74.dp)
                    .clip(shape = RoundedCornerShape(20.dp))
                    .background(Main),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    val intent = Intent(this@ChatView, HomeView::class.java)
                    startActivity(intent)
                    this@ChatView.finish()
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_arrow_back),
                        contentDescription = "Back",
                        tint = Dark
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
                            contentDescription = stringResource(R.string.profilePhoto),
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(4.dp)
                        )
                    }
                    val color: Color
                    if (online)
                        color = Color.Green
                    else
                        color = Color.Red
                    Icon(
                        painter = painterResource(id = R.drawable.ic_circle),
                        contentDescription = stringResource(R.string.customIcon),
                        tint = color, // Comprobar si esta online / offline
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(17.dp)
                            .padding(
                                end = 5.dp, // Ajustamos el espaciado hacia la izquierda
                                bottom = 4.dp
                            )
                    )
                }

                Text(
                    text = nick,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier
                        .padding(start = 40.dp)
                        .weight(1f)
                )



            }
        }
    }
    @Composable
    fun MessageList(chatId: String, messages: Map<String, List<Message>>, token: String, isDarkModeActive: Boolean) {
        val sourceId = sharedPreferencesToken.getString(SOURCE_ID, null)

        var refreshing by remember { mutableStateOf(false) }

        val chatMessages = messages[chatId] ?: emptyList()


        SwipeRefresh(
            state = rememberSwipeRefreshState(refreshing),
            onRefresh = {
                refreshing = true
                MainScope().launch {
                    viewModel.getMessagesForChat(token, chatId, OFFSET_GET_MESSAGESFORCHAT, LIMIT_GET_MESSAGESFORCHAT)
                    delay(DELAY_GET_MESSAGESFORCHAT)
                    refreshing = false
                }
            }
        ) {
            LazyColumn(Modifier.fillMaxSize()) {
                items(chatMessages) { message ->
                    if (message.source == sourceId) {
                        MessageItem(message, true, isDarkModeActive)
                    } else {
                        MessageItem(message, false, isDarkModeActive)
                    }
                }
            }
        }
    }


    @Composable
    fun MessageItem(messages: Message, isSentByUser: Boolean, isDarkModeActive: Boolean) {
        val horizontalPadding = 30.dp
        val verticalPadding = 8.dp


        val backgroundColor = if (isDarkModeActive) Contrast.copy(alpha = 0.4f) else White
        val alignment = if (isSentByUser) TextAlign.Start else TextAlign.End
        val startPadding = if (isSentByUser) horizontalPadding else 0.dp
        val endPadding = if (isSentByUser) 0.dp else horizontalPadding

        val formattedTime = formatTimeFromApiHour(messages.date) ?: "N/A"


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
                        color =  if (isDarkModeActive) White else Color.Black,
                        textAlign = alignment,
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = formattedTime,
                        style = TextStyle(fontSize = 14.sp, color =  if (isDarkModeActive) White else Color.Gray),
                    )
                }
            }


        }
    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun BottomBar(
        chatId: String?,
        isDarkModeActive: Boolean,
        onSendMessage: (String) -> Unit,
    ) {
        var messageText by remember { mutableStateOf("") }
        val token = sharedPreferencesToken.getString(KEY_TOKEN, null)
        val sourceID = sharedPreferencesToken.getString(SOURCE_ID, null)


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 30.dp, top = 10.dp),
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
                placeholder = { Text(text = stringResource(R.string.writeAMessage)) },
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent, // Ocultar la línea de foco
                    unfocusedIndicatorColor = Color.Transparent // Ocultar la línea de enfoque
                )
            )

            IconButton(
                onClick = {

                    if (chatId != null && token != null) {

                        lifecycleScope.launch {
                            viewModel.createNewMessageAndReload(
                                token,
                                MessageRequest(chatId, sourceID!!, messageText)
                            )
                        }
                    }
                    onSendMessage(messageText)
                    messageText = ""

                }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = stringResource(R.string.Send),
                    Modifier.size(40.dp),
                    tint =  if (isDarkModeActive) White else Dark
                )
            }
        }
    }

    private fun formatTimeFromApiHour(dateTimeString: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val date = inputFormat.parse(dateTimeString)
        return outputFormat.format(date!!)
    }

    override fun onBackPressed() {
        @Suppress("DEPRECATION")
        super.onBackPressed()
        val intent = Intent(this@ChatView, HomeView::class.java)
        startActivity(intent)
    }
}