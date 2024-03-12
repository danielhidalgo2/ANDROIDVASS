package com.chat.whatsvass.ui.theme.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chat.whatsvass.R
import com.chat.whatsvass.ui.theme.Claro
import com.chat.whatsvass.ui.theme.Contraste
import com.chat.whatsvass.ui.theme.Oscuro
import com.chat.whatsvass.ui.theme.Principal
import com.chat.whatsvass.ui.theme.Transparencia
import com.chat.whatsvass.ui.theme.White
import com.chat.whatsvass.ui.theme.login.showMessage

class HomeView : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

                
            }
                
            
        }
    }



@Composable
fun TopBarHome(text: String, onSettingsClick: () -> Unit) {
    TopAppBar(
        modifier = Modifier
            .fillMaxWidth(),
        backgroundColor = Principal,
        contentPadding = PaddingValues(horizontal = 16.dp),
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Barra de búsqueda
            TextField(
                value = "",
                onValueChange = { /* Cambiar el valor de búsqueda */ },
                placeholder = {
                    Text(
                        text = "Buscar...",
                        style = TextStyle(color = Color.Gray, fontSize = 12.sp)
                    )
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
                    .height(48.dp) // Altura más pequeña
                    .width(120.dp) // Ancho ajustado
                    .clip(RoundedCornerShape(16.dp)), // Bordes redondeados
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.White, // Fondo blanco
                    cursorColor = Color.Black,
                    textColor = Color.Black,
                    placeholderColor = Color.Gray
                ),
                textStyle = TextStyle(color = Color.Black, fontSize = 12.sp), // Tamaño de texto ajustado
                leadingIcon = {
                    // Icono de búsqueda
                    Icon(
                        painter = painterResource(id = R.drawable.ic_search),
                        contentDescription = "Buscar",
                        tint = Color.Black
                    )
                }
            )

            // Espacio entre la barra de búsqueda y el botón de ajustes
            Spacer(modifier = Modifier.width(16.dp))

            // Botón de ajustes
            IconButton(
                onClick = onSettingsClick,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_settings),
                    contentDescription = "Ajustes",
                    tint = Color.Black
                )
            }
        }
    }
}


@Composable
fun ChatList(nombres: List<String>) {
    LazyColumn {
        items(nombres) { nombre ->
            ChatItem(nombre = nombre)
            Divider(color = Color.Gray, thickness = 1.dp)
        }
    }
}

@Composable
fun ChatItem(nombre: String) {
    Row(
        modifier = Modifier
            .padding(vertical = 12.dp, horizontal = 16.dp)
            .fillMaxWidth()
            .requiredWidth(width = 368.dp)
            .requiredHeight(height = 74.dp)
            .shadow(4.dp, shape = RoundedCornerShape(20.dp))
            .clip(shape = RoundedCornerShape(20.dp))
            .background(Transparencia),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.weight(0.1f))
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(Color.Gray),
            contentAlignment = Alignment.Center
        ) {

            Image(
                painter = painterResource(id = R.drawable.image_person),
                contentDescription = "Foto de perfil",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
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
fun ListaDeNombresPreview() {
    val nombres = listOf("Juan", "María", "Pedro", "Ana", "Luis")
    ChatList(nombres = nombres)
}

@Preview
@Composable
fun TopBarPreview() {
    TopBarHome(text = "") {
        
    }
}






