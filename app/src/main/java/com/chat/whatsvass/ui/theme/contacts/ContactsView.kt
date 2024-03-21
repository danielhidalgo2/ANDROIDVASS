package com.chat.whatsvass.ui.theme.contacts

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chat.whatsvass.R
import com.chat.whatsvass.commons.KEY_ID
import com.chat.whatsvass.commons.KEY_TOKEN
import com.chat.whatsvass.commons.SHARED_USER_DATA
import com.chat.whatsvass.data.domain.model.contacts.Contacts
import com.chat.whatsvass.data.domain.repository.remote.response.create_chat.ChatRequest
import com.chat.whatsvass.ui.theme.Claro
import com.chat.whatsvass.ui.theme.Contraste
import com.chat.whatsvass.ui.theme.Oscuro
import com.chat.whatsvass.ui.theme.Principal
import com.chat.whatsvass.ui.theme.White
import com.chat.whatsvass.ui.theme.chat.ChatView
import com.chat.whatsvass.ui.theme.login.hideKeyboard
import com.chat.whatsvass.ui.theme.settings.SettingsView
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ContactsView : ComponentActivity() {
    private val viewModel: ContactsViewModel by viewModels()
    private lateinit var sharedPreferencesUserData: SharedPreferences

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferencesUserData = getSharedPreferences(SHARED_USER_DATA, Context.MODE_PRIVATE)
        val token = sharedPreferencesUserData.getString(KEY_TOKEN, null)
        val myId = sharedPreferencesUserData.getString(KEY_ID, null)

        setContent {

            // Observar el resultado del ViewModel
            LaunchedEffect(key1 = viewModel) {
                if (token != null) {
                    viewModel.getContacts(token)
                }
            }
            // Observar el resultado del ViewModel y configurar el contenido de la pantalla de inicio
            val contactsResult by viewModel.contactsResult.collectAsState(emptyList())
            ContactsScreen(this, token!!, myId!!, contactsResult, onSettingsClick = {}, viewModel)

        }
        window.decorView.setOnTouchListener { _, _ ->
            hideKeyboard(this)
            false
        }
    }
}

@Composable
fun ContactsScreen(
    context: Context,
    token: String,
    myId: String,
    contacts: List<Contacts>,
    onSettingsClick: () -> Unit,
    viewModel: ContactsViewModel
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TopBarAndList(context, token, myId, contacts, onSettingsClick, viewModel)

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun TopBarAndList(
    context: Context,
    token: String,
    myId: String,
    contacts: List<Contacts>,
    onSettingsClick: () -> Unit,
    viewModel: ContactsViewModel
) {
    val isTextWithOutContactsVisible by viewModel.isTextWithOutVisibleFlow.collectAsState(false)
    var searchText by remember { mutableStateOf(TextFieldValue()) }
    var listSearch by remember { mutableStateOf<List<Contacts>>(emptyList()) }

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


    LazyColumn {

        if (searchText.text.isEmpty()) {
            items(contacts) { contact ->
                ContactItem(context, contact, token, viewModel ,ChatRequest(myId, contact.id))
            }
        } else {
            listSearch =
                contacts.filter { it.nick.contains(searchText.text, ignoreCase = true) }
            items(listSearch) { contact ->
                ContactItem(context, contact, token, viewModel ,ChatRequest(myId, contact.id))
            }
        }
    }

    // Si no hay contactos se muestra: "Sin contactos"
    if (isTextWithOutContactsVisible) {
        Column(
            modifier = Modifier
                .height(400.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Sin contactos",
                fontSize = 22.sp,
                color = Oscuro
            )
        }
    }
    // Si hay contactos y el searchText esta vacio se muestra el progressBar
    if (!isTextWithOutContactsVisible && searchText.text.isEmpty()) {
        Column(
            modifier = Modifier
                .height(400.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                modifier = Modifier.width(64.dp),
                color = Claro,
                trackColor = Oscuro,
            )
        }
    }
    // Si no se encuentra el contacto buscado se muestra texto: "Sin coincidencias"
    if (listSearch.isEmpty() && (!searchText.text.isNullOrEmpty() || searchText.text == "Buscar...")) {
        Column(
            modifier = Modifier
                .height(400.dp)
                .fillMaxSize(),
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

@Composable
fun ContactItem(
    context: Context,
    contact: Contacts,
    token: String,
    viewModel: ContactsViewModel,
    chatRequest: ChatRequest,
) {
    val colorWithOpacity = Contraste.copy(alpha = 0.4f)
    val isNewChatCreated by viewModel.isNewChatCreatedFlow.collectAsState(false)
    val newChat by viewModel.newChatResult.collectAsState(null)

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
                    onPress = {

                              },
                    onTap = {
                        viewModel.createNewChat(context, token, chatRequest)
                        if (isNewChatCreated){
                            val intent = Intent(context, ChatView::class.java)
                            intent
                                .putExtra("ChatID", newChat!!.chat.id)
                                .putExtra("Nick", contact.nick)
                                .putExtra("Online",  contact.online.toString())
                            context.startActivity(intent)
                            Log.d("CHATID",  newChat!!.chat.id)
                        }
                    }
                )
            },
        verticalAlignment = Alignment.CenterVertically,

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
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = contact.nick,
                style = TextStyle(fontSize = 16.sp, color = Oscuro),
            )
        }

        Spacer(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.weight(0.1f))
        Spacer(modifier = Modifier.height(50.dp))
    }
}







