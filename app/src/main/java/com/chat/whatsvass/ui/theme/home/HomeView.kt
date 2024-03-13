package com.chat.whatsvass.ui.theme.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chat.whatsvass.R
import com.chat.whatsvass.ui.theme.Claro
import com.chat.whatsvass.ui.theme.Contraste
import com.chat.whatsvass.ui.theme.Oscuro
import com.chat.whatsvass.ui.theme.Principal
import com.chat.whatsvass.ui.theme.White

class HomeView : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val nombres = listOf("Juan", "María", "Pedro", "Ana", "Luis")

        setContent {

            HomeScreen(nombres = nombres) {

            }
        }


    }
}


@Composable
fun HomeScreen(nombres: List<String>, onSettingsClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            //TopBarHome("Buscar", onSettingsClick)
            TopBarHome()
            ChatList(nombres)
            Spacer(modifier = Modifier.weight(1f))
            FloatingActionButton(
                onClick = { /* Acción al hacer clic en el botón flotante */ },
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(16.dp)
                    .size(56.dp),
                backgroundColor = Principal,
                contentColor = Contraste,
                shape = CircleShape
            ) {
                Text("+", fontSize = 30.sp, color = Contraste)
            }
        }
    }
}

@Composable
fun TopBarHome() {
    var searchText by remember { mutableStateOf(TextFieldValue()) }

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
                onClick = { /* Handle settings button click */ },
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

@Preview
@Composable
fun PreviewTopBar() {
    TopBarHome()
}

@Composable
fun ChatList(nombres: List<String>) {
    LazyColumn {
        items(nombres) { nombre ->
            ChatItem(nombre = nombre)
        }
    }
}


@Composable
fun ChatItem(nombre: String) {
    val colorWithOpacity = Contraste.copy(alpha = 0.4f)

    Row(
        modifier = Modifier
            .padding(vertical = 12.dp, horizontal = 16.dp)
            .fillMaxWidth()
            .clickable { /* Llevar a la pantalla de los chats */ }
            .requiredWidth(width = 368.dp)
            .requiredHeight(height = 74.dp)
            .clip(shape = RoundedCornerShape(20.dp))
            .background(colorWithOpacity),
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
                text = nombre,
                style = TextStyle(fontSize = 16.sp, color = Oscuro)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Último mensaje",
                style = TextStyle(fontSize = 14.sp, color = Claro)
            )
        }


        Spacer(modifier = Modifier.weight(1f))


        Text(

            text = "22:00",
            style = TextStyle(fontSize = 14.sp, color = Claro),
            modifier = Modifier.align(Alignment.CenterVertically)
        )

        Spacer(modifier = Modifier.weight(0.1f))
        Spacer(modifier = Modifier.height(50.dp))
    }
}


@Preview
@Composable
fun HomeScreenPreview() {
    val nombres = listOf("Juan", "María", "Pedro", "Ana", "Luis")
    HomeScreen(nombres = nombres) {

    }
}






