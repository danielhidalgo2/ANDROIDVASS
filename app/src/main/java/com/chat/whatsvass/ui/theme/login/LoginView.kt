package com.chat.whatsvass.ui.theme.login

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
import androidx.compose.material.TextField
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chat.whatsvass.R


class LoginView : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val viewModel: LoginViewModel = viewModel()

            LoginScreen(viewModel = viewModel)
        }
    }
}

@Composable
fun LoginScreen(viewModel: LoginViewModel) {
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
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier.size(218.dp),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.height(16.dp))
            UserTextField(viewModel)
            Spacer(modifier = Modifier.height(16.dp))
            PasswordTextField(viewModel)
            Spacer(modifier = Modifier.height(16.dp))
            LoginButton(onClick = {

                viewModel.loginUser("usuario", "contraseña")
            })
            Spacer(modifier = Modifier.height(16.dp))
            CreateAccountText()
        }
    }
}

@Composable
fun UserTextField(viewModel: LoginViewModel) {
    var user by remember { mutableStateOf("") }
    TextField(
        value = user,
        onValueChange = { user = it },
        label = { androidx.compose.material.Text("Usuario") }
    )
}

@Composable
fun PasswordTextField(viewModel: LoginViewModel) {
    var password by remember { mutableStateOf("") }
    TextField(
        value = password,
        onValueChange = { password = it },
        label = { androidx.compose.material.Text("Contraseña") }
    )
}

@Composable
fun CreateAccountText() {
    androidx.compose.material.Text(
        text = "Crear usuario",
        color = Color.White,
        modifier = Modifier.clickable {

        }
    )
}
@Composable
fun LoginButton(onClick: () -> Unit) {
    androidx.compose.material.Button(onClick = onClick) {
        androidx.compose.material.Text("Login")
    }
}




