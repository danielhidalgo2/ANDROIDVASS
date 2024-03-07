package com.chat.whatsvass

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

class LoginView : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LoginScreen()
        }
    }
}

@Composable
fun LoginScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xff8091f2))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo), // Asegúrate de tener tu logo en res/drawable
                contentDescription = "Logo",
                modifier = Modifier.size(218.dp),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.height(16.dp))
            UserTextField()
            Spacer(modifier = Modifier.height(16.dp))
            PasswordTextField()
            Spacer(modifier = Modifier.height(16.dp))
            LoginButton(onClick = {
                // Lógica para manejar el inicio de sesión
            })
            Spacer(modifier = Modifier.height(16.dp))
            CreateAccountText()
        }
    }
}

@Composable
fun UserTextField() {
    var user by remember { mutableStateOf("") }
    TextField(
        value = user,
        onValueChange = { user = it },
        label = { Text("Usuario") }
    )
}

@Composable
fun PasswordTextField() {
    var password by remember { mutableStateOf("") }
    TextField(
        value = password,
        onValueChange = { password = it },
        label = { Text("Contraseña") }
    )
}

@Composable
fun LoginButton(onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text("Login")
    }
}

@Composable
fun CreateAccountText() {
    Text(
        text = "Crear usuario",
        color = Color.White,
        modifier = Modifier.clickable {
            // Lógica para manejar el click en el texto "Crear usuario"
        }
    )
}

