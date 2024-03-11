package com.chat.whatsvass.ui.theme.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.chat.whatsvass.ui.theme.Principal
import com.chat.whatsvass.ui.theme.White

class HomeView : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTopAppBar(title = "Buscar...", onSearchClicked = { /*TODO*/ }) {
                
            }
                
            
        }
    }



    @Composable
    fun MyTopAppBar(
        title: String,
        onSearchClicked: () -> Unit,
        onSettingsClicked: () -> Unit
    ) {
        TopAppBar(
            title = { Text(text = title) },
            actions = {
                IconButton(onClick = onSettingsClicked) {
                    Icon(imageVector = Icons.Filled.Settings, contentDescription = "Settings")
                }
            },
            backgroundColor = Principal,
            elevation = 4.dp
        )

        // Agregamos la barra de búsqueda junto a la TopAppBar
        Surface(
            color = White,
            modifier = Modifier.size(36.dp), // Tamaño ajustado
            shape = RoundedCornerShape(8.dp), // Bordes redondeados
            elevation = 0.dp // Sin elevación
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search",
                    modifier = Modifier.size(24.dp) // Tamaño del icono de búsqueda
                )
                Text(
                    text = title,
                    modifier = Modifier.padding(start = 8.dp) // Espaciado entre el ícono y el texto
                )
            }
        }
    }





    @Preview
    @Composable
    private fun HomePreview() {
            MyTopAppBar(title = "Buscar...", onSearchClicked = { /*TODO*/ }) {
                
            }
    }
}




