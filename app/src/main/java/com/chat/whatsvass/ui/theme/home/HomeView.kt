package com.chat.whatsvass.ui.theme.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.AlertDialog
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chat.whatsvass.R
import com.chat.whatsvass.commons.CHAT_ID_ARGUMENT
import com.chat.whatsvass.commons.DELAY_GET_MESSAGES
import com.chat.whatsvass.commons.KEY_MODE
import com.chat.whatsvass.commons.KNICK_ARGUMENT
import com.chat.whatsvass.commons.LIMIT_GET_MESSAGES
import com.chat.whatsvass.commons.OFFSET_GET_MESSAGES
import com.chat.whatsvass.commons.ONLINE_ARGUMENT
import com.chat.whatsvass.commons.SHARED_SETTINGS
import com.chat.whatsvass.commons.SHARED_USER_DATA
import com.chat.whatsvass.commons.SOURCE_ID
import com.chat.whatsvass.commons.VIEW_FROM
import com.chat.whatsvass.data.domain.model.chat.Chat
import com.chat.whatsvass.data.domain.model.message.Message
import com.chat.whatsvass.ui.theme.Contrast
import com.chat.whatsvass.ui.theme.Dark
import com.chat.whatsvass.ui.theme.DarkMode
import com.chat.whatsvass.ui.theme.Light
import com.chat.whatsvass.ui.theme.Main
import com.chat.whatsvass.ui.theme.White
import com.chat.whatsvass.ui.theme.chat.ChatView
import com.chat.whatsvass.ui.theme.contacts.ContactsView
import com.chat.whatsvass.ui.theme.login.hideKeyboard
import com.chat.whatsvass.ui.theme.settings.SettingsView
import com.chat.whatsvass.usecases.token.Token
import com.chat.whatsvass.utils.DateTimeUtils
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private lateinit var sharedPreferencesToken: SharedPreferences
private lateinit var sharedPreferencesSettings: SharedPreferences

class HomeView : ComponentActivity() {
    private val viewModel: HomeViewModel by viewModels()

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.decorView.apply {
            @Suppress("DEPRECATION")
            systemUiVisibility =
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        }

        sharedPreferencesToken = getSharedPreferences(SHARED_USER_DATA, Context.MODE_PRIVATE)
        val token = Token.token

        sharedPreferencesSettings = getSharedPreferences(SHARED_SETTINGS, Context.MODE_PRIVATE)
        val isDarkModeActive = sharedPreferencesSettings.getBoolean(KEY_MODE, false)


        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)

        setContent {
            LaunchedEffect(key1 = viewModel) {
                if (token != null) {
                    viewModel.getChats(token)
                }
            }

            val chats by viewModel.chats.collectAsState(emptyList())
            val messages by viewModel.messages.collectAsState(emptyMap())
            var chatIds: List<String>

            LaunchedEffect(key1 = chats) {
                if (token != null && chats.isNotEmpty()) {
                    chatIds = chats.map { it.chatId }
                    viewModel.getMessages(token, chatIds, OFFSET_GET_MESSAGES, LIMIT_GET_MESSAGES)
                }
            }

            HomeScreen(
                chats = chats,
                messages = messages,
                viewModel,
                token!!,
                isDarkModeActive,
                onDeleteChat = { chatId ->
                    // Lógica para eliminar el chat en el ViewModel
                    viewModel.deleteChat(token, chatId)
                }
            )
        }


        window.decorView.setOnTouchListener { _, _ ->
            hideKeyboard(this)
            false
        }

    }

    override fun onResume() {
        super.onResume()
        val token = Token.token
        if (token != null) {
            // Actualizar el estado en línea del usuario como "en línea" cuando se reanuda la actividad
            viewModel.updateUserOnlineStatus(token, true)
        }
    }

    override fun onPause() {
        super.onPause()
        val token = Token.token
        if (token != null) {
            viewModel.updateUserOnlineStatus(token, false)
        }
    }
}

@Composable
fun HomeScreen(
    chats: List<Chat>,
    messages: Map<String, List<Message>>,
    viewModel: HomeViewModel,
    token: String,
    isDarkModeActive: Boolean,
    onDeleteChat: (chatId: String) -> Unit
) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(if (isDarkModeActive) DarkMode else White)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TopBarHomeAndList(chats, messages, viewModel, token, isDarkModeActive, onDeleteChat)
            Spacer(modifier = Modifier.weight(1f))

        }
        FloatingActionButton(
            onClick = {
                val intent = Intent(context, ContactsView::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(horizontal = 16.dp)
                .padding(bottom = 35.dp)
                .size(56.dp),
            backgroundColor = Main,
            contentColor = Contrast,
            shape = CircleShape
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_add),
                contentDescription = stringResource(R.string.add),
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Composable
fun ChatItem(
    chat: Chat,
    messages: List<Message?>,
    name: String,
    color: Color,
    isDarkModeActive: Boolean,
    onDeleteChat: (chatId: String) -> Unit
) {
    val colorWithOpacity = Contrast.copy(alpha = 0.4f)
    val context = LocalContext.current
    val mutableColor = remember {
        mutableStateOf(false)
    }
    mutableColor.value = color == Color.Green

    val sourceID = sharedPreferencesToken.getString(SOURCE_ID, null)

    val lastMessage = messages.lastOrNull()

    val formattedTime = lastMessage?.date?.let { DateTimeUtils().formatTimeFromApi(it, context) } ?: ""

    val showDialog = remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .padding(vertical = 12.dp, horizontal = 16.dp)
            .fillMaxWidth()
            .requiredWidth(width = 368.dp)
            .requiredHeight(height = 74.dp)
            .clip(shape = RoundedCornerShape(20.dp))
            .background(colorWithOpacity)
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        showDialog.value = true
                    },
                    onTap = {
                        val intent = Intent(context, ChatView::class.java)
                        intent
                            .putExtra(CHAT_ID_ARGUMENT, chat.chatId)
                            .putExtra(KNICK_ARGUMENT, name)
                            .putExtra(ONLINE_ARGUMENT, mutableColor.value.toString())
                        context.startActivity(intent)
                    }
                )
            },
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
                    contentDescription = stringResource(id = R.string.profilePhoto),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(4.dp)
                )
            }

            Icon(
                painter = painterResource(id = R.drawable.ic_circle),
                contentDescription = stringResource(id = R.string.customIcon),
                tint = color, // Comprobar si esta online / offline
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(17.dp)
                    .padding(
                        end = 5.dp,
                        bottom = 4.dp
                    )

            )
            mutableColor.value = color == Color.Green
        }
        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = name,
                style = TextStyle(
                    fontSize = 16.sp,
                    color = if (isDarkModeActive) White else Dark,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = lastMessage?.message?.let { if (it.length > 15) it.take(12) + "..." else it }
                    ?: stringResource(
                        id = R.string.thereAreNoMessages
                    ),
                style = TextStyle(fontSize = 14.sp, color = if (isDarkModeActive) White else Light),
                maxLines = 1
            )
        }


        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = formattedTime,
            style = TextStyle(fontSize = 14.sp, color = if (isDarkModeActive) White else Light),
            textAlign = TextAlign.End
        )

        Spacer(modifier = Modifier.weight(0.1f))
        Spacer(modifier = Modifier.height(50.dp))
        if (showDialog.value) {
            AlertDialog(
                onDismissRequest = { showDialog.value = false },
                title = { Text(stringResource(R.string.deleteChat)) },
                text = { Text(stringResource(R.string.areYouSureToDeleteChat)) },
                confirmButton = {
                    Button(
                        onClick = {
                            if (chat.sourceId == sourceID) {
                                showDialog.value = false
                                onDeleteChat(chat.chatId)
                            } else {
                                Toast.makeText(context, "No puedes eliminar este chat", Toast.LENGTH_SHORT).show()
                            }
                        }
                    ) {
                        Text(stringResource(R.string.yes))
                    }
                },
                dismissButton = {
                    Button(
                        onClick = { showDialog.value = false }
                    ) {
                        Text(stringResource(R.string.cancel))
                    }
                }
            )
        }
    }
}

@Composable
fun TopBarHomeAndList(
    chats: List<Chat>,
    messages: Map<String, List<Message>>,
    viewModel: HomeViewModel,
    token: String,
    isDarkModeActive: Boolean,
    onDeleteChat: (chatId: String) -> Unit
) {
    val isTextWithOutChatsVisible by viewModel.isTextWithOutChatsVisibleFlow.collectAsState(false)
    val isProgressBarVisible by viewModel.isProgressVisibleFlow.collectAsState(true)
    var searchText by remember { mutableStateOf(TextFieldValue()) }
    var listSearch by remember { mutableStateOf<List<Chat>>(emptyList()) }
    val chatsUpdates by viewModel.chats.collectAsState(emptyList())

    val context = LocalContext.current
    var refreshing by remember { mutableStateOf(false) }

    val listResultOrdered = DateTimeUtils().orderChatsByDate(chats, messages)

    TopAppBar(
        backgroundColor = Main,
        elevation = 4.dp,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(White)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_search),
                        contentDescription = stringResource(id = R.string.search),
                        tint = Color.Black,
                        modifier = Modifier.size(24.dp)
                    )
                    BasicTextField(
                        value = searchText.text,
                        onValueChange = { searchText = TextFieldValue(it) },
                        textStyle = MaterialTheme.typography.body1,
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        decorationBox = { innerTextField ->
                            Box(
                                contentAlignment = Alignment.CenterStart,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            ) {
                                if (searchText.text.isEmpty()) {
                                    Text(
                                        text = stringResource(id = R.string.textFieldSearch),
                                        style = MaterialTheme.typography.body1,
                                        color = Color.Gray
                                    )
                                }
                                innerTextField()
                            }
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.width(80.dp))

            IconButton(
                onClick = {
                    val intent = Intent(context, SettingsView::class.java)
                    intent.flags = (Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    intent.putExtra(VIEW_FROM, "Home")
                    context.startActivity(intent)
                },
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_settings),
                    contentDescription = stringResource(id = R.string.settings),
                    tint = Color.Black,
                    modifier = Modifier.size(36.dp)
                )
            }
        }
    }

    SwipeRefresh(
        state = rememberSwipeRefreshState(refreshing),
        onRefresh = {
            refreshing = true
            MainScope().launch {
                val listOfChatsIds = mutableListOf<String>()
                for (i in chatsUpdates) {
                    listOfChatsIds.add(i.chatId)
                }
                viewModel.getChats(token)
                viewModel.getMessages(
                    token,
                    listOfChatsIds,
                    OFFSET_GET_MESSAGES,
                    LIMIT_GET_MESSAGES
                )
                delay(DELAY_GET_MESSAGES)
                refreshing = false
            }
        }
    ) {
        Box(Modifier.fillMaxSize()) {
            var chatsNew = chats.toMutableList()
            if (!isProgressBarVisible) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {

                    for (i in chats) {
                        for (j in listResultOrdered) {
                            if (i.chatId == j) {
                                chatsNew.removeAt(listResultOrdered.indexOf(j))
                                chatsNew.add(listResultOrdered.indexOf(j), i)
                            }
                        }
                    }
                    chatsNew = chatsNew.distinct().toMutableList()

                    if (searchText.text.isEmpty() || searchText.text == R.string.textFieldSearch.toString()) {
                        items(
                            items = chatsNew,
                            key = { chat ->
                                chat.chatId
                            }
                        ) { chat ->
                            val chatMessages = messages[chat.chatId] ?: emptyList()
                            val sourceId = sharedPreferencesToken.getString(SOURCE_ID, null)
                            val name =
                                if (chat.sourceId == sourceId) chat.targetNick else chat.sourceNick
                            val color: Color =
                                if (if (chat.sourceId == sourceId) chat.targetOnline else chat.sourceOnline) {
                                    Color.Green
                                } else {
                                    Color.Red
                                }

                            ChatItem(
                                chat = chat,
                                messages = chatMessages,
                                name = name,
                                color = color,
                                isDarkModeActive = isDarkModeActive,
                                onDeleteChat = { onDeleteChat(chat.chatId) })
                        }
                    } else {
                        listSearch =
                            chatsNew.filter { chat ->
                                val sourceId = sharedPreferencesToken.getString(SOURCE_ID, null)
                                if (chat.sourceId == sourceId) {
                                    chat.targetNick.contains(
                                        searchText.text,
                                        ignoreCase = true
                                    )
                                } else {
                                    chat.sourceNick.contains(
                                        searchText.text,
                                        ignoreCase = true
                                    )
                                }
                            }
                        items(listSearch,
                            key = { chat ->
                                // La llave sirve para que cada valor se mueva con su celda
                                chat.chatId
                            }) { chat ->
                            val chatMessages = messages[chat.chatId] ?: emptyList()
                            val sourceId = sharedPreferencesToken.getString(SOURCE_ID, null)
                            val name =
                                if (chat.sourceId == sourceId) chat.targetNick else chat.sourceNick
                            val color: Color =
                                if (if (chat.sourceId == sourceId) chat.targetOnline else chat.sourceOnline) {
                                    Color.Green
                                } else {
                                    Color.Red
                                }
                            ChatItem(
                                chat = chat,
                                messages = chatMessages,
                                name = name,
                                color = color,
                                isDarkModeActive = isDarkModeActive,
                                onDeleteChat = { onDeleteChat(chat.chatId) })
                        }
                    }
                }
            }

            val textColor = if (isDarkModeActive) White else Dark
            if (chatsNew.isNotEmpty() && listSearch.isEmpty() && (searchText.text.isNotEmpty() || searchText.text == R.string.textFieldSearch.toString())) {
                Column(
                    modifier = Modifier
                        .height(400.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(id = R.string.noMatches),
                        fontSize = 22.sp,
                        color = textColor
                    )
                }
            }
            if (isTextWithOutChatsVisible && chatsNew.isEmpty()) {
                Column(
                    modifier = Modifier
                        .height(400.dp)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(id = R.string.thereAreNoChats),
                        fontSize = 22.sp,
                        color = textColor
                    )
                }
            }
            if (isProgressBarVisible && searchText.text.isEmpty()) {
                Column(
                    modifier = Modifier
                        .height(400.dp)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.width(64.dp),
                        color = Light,
                        trackColor = Dark,
                    )
                }
            }
        }
    }
}