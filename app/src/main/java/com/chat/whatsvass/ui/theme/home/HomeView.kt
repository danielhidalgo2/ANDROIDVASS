package com.chat.whatsvass.ui.theme.home

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
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
import com.chat.whatsvass.ui.theme.settings.SettingsView
import java.text.SimpleDateFormat
import java.util.Locale

class HomeView : ComponentActivity() {
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var sharedPreferencesToken: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferencesToken = getSharedPreferences(SHARED_TOKEN, Context.MODE_PRIVATE)
        val token = sharedPreferencesToken.getString(KEY_TOKEN, null)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // No hagas nada cuando se presiona el botón de retroceso
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)

        setContent {
            // Observar el resultado del ViewModel para obtener los chats
            LaunchedEffect(key1 = viewModel) {
                if (token != null) {
                    viewModel.getChats(token)
                    Log.d("HomeView", "Obteniendo chats con token: $token")
                }
            }

            // Observar el resultado del ViewModel y configurar el contenido de la pantalla de inicio
            val chats by viewModel.chats.collectAsState(emptyList())
            val messages by viewModel.messages.collectAsState(emptyMap())
            val navController = rememberNavController()

            // Llamar a la función getMessages después de obtener los chats
            LaunchedEffect(key1 = chats) {
                if (token != null && chats.isNotEmpty()) {
                    val chatIds = chats.map { it.chatId }
                    viewModel.getMessages(token, chatIds, offset = 0, limit = 1)
                }
            }

            HomeScreen(
                chats = chats,
                messages = messages,
                navigation = navController,
                onDeleteChat = { chatId ->
                    // Lógica para eliminar el chat en el ViewModel
                    viewModel.deleteChat(token!!, chatId)
                }
            )
        }
    }
}




@Composable
fun HomeScreen(
    chats: List<Chat>,
    messages: Map<String, List<Message>>,
    navigation: NavController,
    onDeleteChat: (chatId: String) -> Unit // Agregar parámetro onDeleteChat
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TopBarHome(navigation)
            ChatList(chats = chats, messages = messages, onDeleteChat = onDeleteChat)

            Spacer(modifier = Modifier.weight(1f))
            FloatingActionButton(
                onClick = { navigation.navigate("lista_usuarios") },
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(16.dp)
                    .size(56.dp),
                backgroundColor = Principal,
                contentColor = Contraste,
                shape = CircleShape
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add),
                    contentDescription = "add",
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}


@Composable
fun ChatList(
    chats: List<Chat>,
    messages: Map<String, List<Message>>,
    onDeleteChat: (chatId: String) -> Unit // Agregar parámetro onDeleteChat
) {
    LazyColumn {
        items(chats) { chat ->
            val chatMessages = messages[chat.chatId] ?: emptyList()
            ChatItem(chat = chat, messages = chatMessages, onDeleteChat = { onDeleteChat(chat.chatId) })
        }
    }
}


fun formatTimeFromApi(dateTimeString: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    val outputFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
    val date = inputFormat.parse(dateTimeString)
    return outputFormat.format(date)
}

@Composable
fun ChatItem(
    chat: Chat,
    messages: List<Message>,
    onDeleteChat: (chatId: String) -> Unit // Modificación del parámetro onDeleteChat
) {
    val colorWithOpacity = Contraste.copy(alpha = 0.4f)

    // Obtener el último mensaje si existe
    val lastMessage = messages.lastOrNull()

    // Formatear la fecha del mensaje para mostrar solo la hora
    val formattedTime = lastMessage?.date?.let { formatTimeFromApi(it) } ?: "N/A"

    // Estado para controlar si el diálogo está mostrándose
    val showDialog = remember { mutableStateOf(false) }

    // Agregar un evento LongPress para mostrar el diálogo de confirmación
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
                    }
                )
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.weight(0.1f))
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

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = chat.sourceNick,
                style = TextStyle(fontSize = 16.sp, color = Oscuro),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = lastMessage?.message ?: "No hay mensajes",
                style = TextStyle(fontSize = 14.sp, color = Claro)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = formattedTime,
            style = TextStyle(fontSize = 14.sp, color = Claro),
            modifier = Modifier.align(Alignment.CenterVertically)
        )

        Spacer(modifier = Modifier.weight(0.1f))
        Spacer(modifier = Modifier.height(50.dp))

        // Diálogo de confirmación
        if (showDialog.value) {
            AlertDialog(
                onDismissRequest = { showDialog.value = false },
                title = { Text("Eliminar chat") },
                text = { Text("¿Estás seguro de que quieres eliminar este chat?") },
                confirmButton = {
                    Button(
                        onClick = {
                            showDialog.value = false
                            onDeleteChat(chat.chatId) // Llamada al callback con el chatId
                        }
                    ) {
                        Text("Sí")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = { showDialog.value = false }
                    ) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}





@Composable
fun TopBarHome(navigation: NavController) {
    var searchText by remember { mutableStateOf(TextFieldValue()) }
    val context = LocalContext.current

    TopAppBar(
        backgroundColor = Principal,
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
                    .clickable { /* Define action when search icon is clicked */ }

            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_search),
                        contentDescription = "Search",
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
                                        text = "Buscar...",
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
                    context.startActivity(intent)
                },
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_settings),
                    contentDescription = "Settings",
                    tint = Color.Black,
                    modifier = Modifier.size(36.dp)
                )
            }
        }
    }
}




