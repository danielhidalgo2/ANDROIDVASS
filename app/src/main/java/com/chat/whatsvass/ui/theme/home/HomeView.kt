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
fun ListaDeNombres(nombres: List<String>) {
    LazyColumn {
        items(nombres) { nombre ->
            ListaItem(nombre = nombre)
            Divider(color = Color.Gray, thickness = 1.dp)
        }
    }
}

@Composable
fun ListaItem(nombre: String) {
    Row(
        modifier = Modifier
            .padding(vertical = 12.dp, horizontal = 16.dp) // Margen dentro del ítem
            .fillMaxWidth()
            .requiredWidth(width = 368.dp) // Ancho requerido
            .requiredHeight(height = 74.dp) // Altura requerida
            .shadow(4.dp, shape = RoundedCornerShape(20.dp)) // Sombrero para efecto de flotación
            .clip(shape = RoundedCornerShape(20.dp)) // Recorte redondeado
            .background(Transparencia), // Color de fondo
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
            // Aquí puedes colocar la imagen de perfil
            // Por ejemplo: Image(...)
            Image(
                painter = painterResource(id = R.drawable.image_person), // Cambia por la imagen de perfil real
                contentDescription = "Foto de perfil",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        // Espacio entre la foto de perfil y el nombre
        Spacer(modifier = Modifier.width(16.dp))

        // Columna para el nombre y el texto "Último mensaje"
        Column(
            modifier = Modifier.weight(1f) // Para ocupar todo el espacio restante
        ) {
            // Nombre
            Text(
                text = nombre,
                style = TextStyle(fontSize = 16.sp, color = Oscuro)
            )

            // Espacio entre el nombre y el texto "Último mensaje"
            Spacer(modifier = Modifier.height(8.dp))

            // Texto "Último mensaje"
            Text(
                text = "Último mensaje",
                style = TextStyle(fontSize = 14.sp, color = Claro)
            )
        }

        // Espacio entre el texto "Último mensaje" y la hora
        Spacer(modifier = Modifier.weight(1f))

        // Texto de la hora
        Text(

            text = "22:00",
            style = TextStyle(fontSize = 14.sp, color = Claro),
            modifier = Modifier.align(Alignment.CenterVertically) // Alineación vertical con el nombre
        )

        Spacer(modifier = Modifier.weight(0.1f))
        Spacer(modifier = Modifier.height(50.dp))
    }
}



























@Preview
@Composable
fun ListaDeNombresPreview() {
    val nombres = listOf("Juan", "María", "Pedro", "Ana", "Luis")
    ListaDeNombres(nombres = nombres)
}

@Preview
@Composable
fun TopBarPreview() {
    TopBarHome(text = "") {
        
    }
}






