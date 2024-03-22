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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TopAppBar
import androidx.compose.material.minimumInteractiveComponentSize
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
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
import com.chat.whatsvass.commons.SHARED_USER_DATA
import com.chat.whatsvass.commons.SOURCE_ID
import com.chat.whatsvass.data.domain.model.chat.Chat
import com.chat.whatsvass.data.domain.model.message.Message
import com.chat.whatsvass.ui.theme.Claro
import com.chat.whatsvass.ui.theme.Contraste
import com.chat.whatsvass.ui.theme.Oscuro
import com.chat.whatsvass.ui.theme.Principal
import com.chat.whatsvass.ui.theme.White
import com.chat.whatsvass.ui.theme.chat.ChatView
import com.chat.whatsvass.ui.theme.login.hideKeyboard
import com.chat.whatsvass.ui.theme.contacts.ContactsView
import com.chat.whatsvass.ui.theme.settings.SettingsView
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.internal.notify
import okhttp3.internal.notifyAll
import java.text.SimpleDateFormat
import java.util.Collections
import java.util.Locale
import java.util.Random

private lateinit var sharedPreferencesToken: SharedPreferences

class HomeView : ComponentActivity() {
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferencesToken = getSharedPreferences(SHARED_USER_DATA, Context.MODE_PRIVATE)
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
            var chatIds = listOf<String>()

            // Llamar a la función getMessages después de obtener los chats
            LaunchedEffect(key1 = chats) {
                if (token != null && chats.isNotEmpty()) {
                    chatIds = chats.map { it.chatId }
                    viewModel.getMessages(token, chatIds, offset = 0, limit = 1)
                }
            }

            HomeScreen(
                chats = chats,
                messages = messages,
                navigation = navController,
                viewModel,
                token!!,
                onDeleteChat = { chatId ->
                    // Lógica para eliminar el chat en el ViewModel
                    viewModel.deleteChat(token!!, chatId)
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
        // Obtener el token de SharedPreferences
        val token = sharedPreferencesToken.getString(KEY_TOKEN, null)
        if (token != null) {
            // Actualizar el estado en línea del usuario como "en línea" cuando se reanuda la actividad
            viewModel.updateUserOnlineStatus(token, true)
        }

    }

    override fun onPause() {
        super.onPause()
        // Obtener el token de SharedPreferences
        val token = sharedPreferencesToken.getString(KEY_TOKEN, null)
        if (token != null) {
            // Actualizar el estado en línea del usuario como "fuera de línea" cuando se pausa la actividad
            viewModel.updateUserOnlineStatus(token, false)
        }

    }

}


@Composable
fun HomeScreen(
    chats: List<Chat>,
    messages: Map<String, List<Message>>,
    navigation: NavController,
    viewModel: HomeViewModel,
    token: String,
    onDeleteChat: (chatId: String) -> Unit // Agregar parámetro onDeleteChat
) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TopBarHomeAndList(chats, messages, viewModel, token, onDeleteChat)
            Spacer(modifier = Modifier.weight(1f))

        }
        FloatingActionButton(
            onClick = {
                val intent = Intent(context, ContactsView::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
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


@Composable
fun ChatList(
    chats: List<Chat>,
    messages: Map<String, List<Message>>,
    onDeleteChat: (chatId: String) -> Unit // Agregar parámetro onDeleteChat
) {
    LazyColumn {
        items(chats) { chat ->
            val chatMessages = messages[chat.chatId] ?: emptyList()
            val sourceId = sharedPreferencesToken.getString(SOURCE_ID, null)
            val name = if (chat.sourceId == sourceId) chat.targetNick else chat.sourceNick
            val color: Color =
                if (if (chat.sourceId == sourceId) chat.targetOnline else chat.sourceOnline) {
                    Color.Green // Si el online del target o del source es true, asigna "verde" a la variable color
                } else {
                    Color.Red // Si no, asigna otro color
                }
            ChatItem(
                chat = chat,
                messages = chatMessages,
                name = name,
                color = color,
                onDeleteChat = { onDeleteChat(chat.chatId) })
        }
    }
}

fun formatTimeFromApi(dateTimeString: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    val outputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val date = inputFormat.parse(dateTimeString)
    return outputFormat.format(date!!)
}
fun formatTimeFromApiToOrderList(dateTimeString: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    val outputFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    val date = inputFormat.parse(dateTimeString)
    return outputFormat.format(date!!)
}

@Composable
fun ChatItem(
    chat: Chat,
    messages: List<Message?>,
    name: String,
    color: Color,
    onDeleteChat: (chatId: String) -> Unit // Modificación del parámetro onDeleteChat
) {
    val colorWithOpacity = Contraste.copy(alpha = 0.4f)
    val context = LocalContext.current
    val online: Boolean = color == Color.Green

    // Obtener el último mensaje si existe
    val lastMessage = messages!!.lastOrNull()

    // Formatear la fecha del mensaje para mostrar solo la hora
    val formattedTime = lastMessage?.date?.let { formatTimeFromApi(it) } ?: "N/A"

    // Estado para controlar si el diálogo está mostrándose
    val showDialog = remember { mutableStateOf(false) }

    // Agregar un evento LongPress para mostrar el diálogo de confirmación
    Row(
        modifier = Modifier
            .padding(vertical = 12.dp, horizontal = 16.dp)
            .fillMaxWidth()
            .clickable { }
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
                            .putExtra("ChatID", chat.chatId)
                            .putExtra("Nick", name)
                            .putExtra("Online", online.toString())
                        context.startActivity(intent)
                        Log.d("chatid", chat.chatId)
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
                    contentDescription = "Foto de perfil",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(4.dp)
                )
            }

            Icon(
                painter = painterResource(id = R.drawable.ic_circle),
                contentDescription = "Custom Icon",
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



        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = name,
                style = TextStyle(fontSize = 16.sp, color = Oscuro),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = lastMessage?.message?.let { if (it.length > 15) it.take(15) + "..." else it } ?: "No hay mensajes",
                style = TextStyle(fontSize = 14.sp, color = Claro),
                maxLines = 1
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


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TopBarHomeAndList(
    chats: List<Chat>,
    messages: Map<String, List<Message>>,
    viewModel: HomeViewModel,
    token: String,
    onDeleteChat: (chatId: String) -> Unit // Agregar parámetro onDeleteChat
) {
    var searchText by remember { mutableStateOf(TextFieldValue()) }
    var listSearch by remember { mutableStateOf<List<Chat>>(emptyList()) }
    val chatsUpdates by viewModel.chats.collectAsState(emptyList())

    val context = LocalContext.current
    var refreshing by remember { mutableStateOf(false) }

    // Ordenar chats por hora de ultimo mensaje
    var mapOfChatIDandDateLast = mutableMapOf<String, String>()
    for (i in chats) {
        if (!messages[i.chatId].isNullOrEmpty()) {
            if (!messages[i.chatId]!!.lastOrNull()!!.date.isNullOrEmpty()) {
                val formattedTime =
                    messages[i.chatId]!!.lastOrNull()!!.date.let { formatTimeFromApiToOrderList(it) }
                mapOfChatIDandDateLast[i.chatId] = formattedTime
            }
        } else {
            mapOfChatIDandDateLast[i.chatId] = "0"
        }
    }
    val mapResultOrdered =
        mapOfChatIDandDateLast.toList().sortedByDescending{ (_, value) -> value }.toMap()
    val listRestultOrdered = mapResultOrdered.keys.toList()
    Log.d("Mensajes ordenados", listRestultOrdered.toString())

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

    SwipeRefresh(
        state = rememberSwipeRefreshState(refreshing),
        onRefresh = {
            refreshing = true
            MainScope().launch {
                var listOfChatsIds = mutableListOf<String>()
                for (i in chatsUpdates) {
                    listOfChatsIds.add(i.chatId)
                }
                viewModel.getChats(token)
                viewModel.getMessages(token, listOfChatsIds, 0, 1)
                delay(1000)
                refreshing = false
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            //Nueva lista con los chats ordenados por fecha y hora
            var chatsNew = chats.toMutableList()
            for (i in chats){
                for (j in listRestultOrdered){
                    if (i.chatId == j ){
                        chatsNew.removeAt(listRestultOrdered.indexOf(j))
                        chatsNew.add(listRestultOrdered.indexOf(j), i)
                    }
                }
            }
            chatsNew = chatsNew.distinct().toMutableList()

            for (i in chatsNew){
                Log.d("CHATNEW", i.chatId)
            }
            if (searchText.text.isNullOrEmpty() || searchText.text == "Buscar...") {
                items(
                    items = chatsNew,
                    key = { chat ->
                        // La llave sirve para que cada valor se mueva con su celda
                       chat.chatId
                    }
                ){ chat ->
                    val chatMessages = messages[chat.chatId] ?: emptyList()
                    val sourceId = sharedPreferencesToken.getString(SOURCE_ID, null)
                    val name = if (chat.sourceId == sourceId) chat.targetNick else chat.sourceNick
                    val color: Color = if (if (chat.sourceId == sourceId) chat.targetOnline else chat.sourceOnline) {
                        Color.Green // Si el online del target o del source es true, asigna "verde" a la variable color
                    } else {
                        Color.Red // Si no, asigna otro color
                    }
                    ChatItem(
                        chat = chat,
                        messages = chatMessages,
                        name = name,
                        color = color,
                        onDeleteChat = { onDeleteChat(chat.chatId) })
                }
            } else {
                listSearch =
                    chatsNew.filter { chat ->
                        val sourceId = sharedPreferencesToken.getString(SOURCE_ID, null)
                        if (chat.sourceId == sourceId){
                            chat.targetNick.contains(searchText.text,
                                ignoreCase = true)
                        } else {
                            chat.sourceNick.contains(searchText.text,
                                ignoreCase = true)
                        }
                    }
                items(listSearch,
                    key = { chat ->
                        // La llave sirve para que cada valor se mueva con su celda
                        chat.chatId
                    }) { chat ->
                    val chatMessages = messages[chat.chatId] ?: emptyList()
                    val sourceId = sharedPreferencesToken.getString(SOURCE_ID, null)
                    val name = if (chat.sourceId == sourceId) chat.targetNick else chat.sourceNick
                    val color: Color = if (if (chat.sourceId == sourceId) chat.targetOnline else chat.sourceOnline) {
                        Color.Green // Si el online del target o del source es true, asigna "verde" a la variable color
                    } else {
                        Color.Red // Si no, asigna otro color
                    }
                    ChatItem(
                        chat = chat,
                        messages = chatMessages,
                        name = name,
                        color = color,
                        onDeleteChat = { onDeleteChat(chat.chatId) })
                }
            }
        }
    }


    // Si no se encuentra el contacto buscado se muestra texto: "Sin coincidencias"
    if (listSearch.isEmpty() && (!searchText.text.isNullOrEmpty() || searchText.text == "Buscar...")) {
        Column(
            modifier = Modifier
                .height(600.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Sin coincidencias",
                fontSize = 22.sp,
                color = Oscuro
            )
        }
    }
}



