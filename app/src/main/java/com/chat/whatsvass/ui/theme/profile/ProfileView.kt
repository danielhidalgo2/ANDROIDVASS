package com.chat.whatsvass.ui.theme.profile

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.chat.whatsvass.R
import com.chat.whatsvass.ui.theme.Main
import com.chat.whatsvass.ui.theme.components.GeneralComponents.ButtonCustom
import com.chat.whatsvass.ui.theme.components.GeneralComponents.NavigationBarCustom
import com.chat.whatsvass.ui.theme.components.GeneralComponents.PasswordTextFieldCustom
import com.chat.whatsvass.ui.theme.components.GeneralComponents.TextFieldCustom
import com.chat.whatsvass.ui.theme.loading.LoadingActivity
import com.chat.whatsvass.ui.theme.login.hideKeyboard
import com.chat.whatsvass.ui.theme.login.showMessage


class ProfileView : ComponentActivity() {

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

//            val navController = rememberNavController()
//            ProfileScreen(ProfileViewModel(), navController)

        }
        window.decorView.setOnTouchListener { _, _ ->
            hideKeyboard(this)
            false
        }

    }

    @Composable
    fun ProfileScreen(viewModel: ProfileViewModel, navController: NavController) {

        // Cambiar color de statusBar en compose
        /*   val systemUiController = rememberSystemUiController()
           systemUiController.setStatusBarColor(
               color = Principal
           )*/

        val context = LocalContext.current
        val registerResult by viewModel.registerResult.collectAsState()
        val keyboardController = LocalSoftwareKeyboardController.current
        //Prueba

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xff8091f2))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                NavigationBarCustom(
                    text = "Crear Perfil",
                    onBackClick = { navController.popBackStack() })
                Spacer(modifier = Modifier.height(20.dp))
                ImageProfile()
                Spacer(modifier = Modifier.height(40.dp))
                val user = TextFieldCustom(
                    "Usuario",
                    onImeActionPerformed = { action ->
                        if (action == ImeAction.Done || action == ImeAction.Next) {
                            keyboardController?.hide()
                        }
                    },
                )
                Spacer(modifier = Modifier.height(20.dp))
                val nick = TextFieldCustom(
                    "Nick",
                    onImeActionPerformed = { action ->
                        if (action == ImeAction.Done || action == ImeAction.Next) {
                            keyboardController?.hide()
                        }
                    },
                )
                Spacer(modifier = Modifier.height(20.dp))
                val password = PasswordTextFieldCustom(
                    "Contraseña",
                    onImeActionPerformed = { action ->
                        if (action == ImeAction.Done || action == ImeAction.Next) {
                            keyboardController?.hide()
                        }
                    },
                )
                Spacer(modifier = Modifier.height(20.dp))
                val confirmPassword = PasswordTextFieldCustom(
                    "Repetir Contraseña",
                    onImeActionPerformed = { action ->
                        if (action == ImeAction.Done || action == ImeAction.Next) {
                            viewModel.registerUser(user, nick, password)
                            keyboardController?.hide()
                        }
                    },
                )
                Spacer(modifier = Modifier.weight(0.3f))
                ButtonCustom(
                    onClick = {
                        if (user.isNullOrEmpty()) {
                            Toast.makeText(
                                context,
                                "Ingresa tu nombre de usuario",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        } else if (nick.isNullOrEmpty()) {
                            Toast.makeText(context, "Ingresa tu nick", Toast.LENGTH_SHORT).show()
                        } else if (password.isNullOrEmpty()) {
                            Toast.makeText(context, "Ingresa una contraseña", Toast.LENGTH_SHORT)
                                .show()
                        } else if (confirmPassword.isNullOrEmpty()) {
                            Toast.makeText(context, "Confirma tu contraseña", Toast.LENGTH_SHORT)
                                .show()
                        } else if (password != confirmPassword) {
                            Toast.makeText(
                                context,
                                "Las contraseñas deben ser iguales",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            Log.d("contraseña", password)
                        } else {
                            viewModel.registerUser(user, password, nick)
                            Log.d("contraseña", password)
                        }

                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 95.dp)
                        .height(60.dp),
                    text = "Crear Usuario"
                )
                Spacer(modifier = Modifier.height(40.dp))
            }
        }

        if (registerResult != null) {
            // Manejar el resultado del inicio de sesión aquí
            when (registerResult) {
                is ProfileViewModel.RegisterResult.Success -> {
                    (registerResult as ProfileViewModel.RegisterResult.Success).register
                    showMessage(
                        context,
                        "Usuario creado correctamente."
                    )
                    //  IR HACIA HOME
                    val intent = Intent(context, LoadingActivity::class.java)
                    context.startActivity(intent)

                }

                is ProfileViewModel.RegisterResult.Error -> {
                    val errorMessage =
                        (registerResult as ProfileViewModel.RegisterResult.Error).message
                    showMessage(context, "No se pudo crear el usuario, intente nuevamente.")
                    Log.d("Error al crear usuario", "Error al crear usuario: $errorMessage")
                }

                else -> {}
            }
        }
    }




    @Composable
    fun ImageProfile() {
        val selectedImage = remember { mutableStateOf<Uri?>(null) }

        // Abrir galeria
        val getContent = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
            selectedImage.value = uri
        }
        
        Box(
            modifier = Modifier
                .height(152.dp)
                .width(152.dp)
        ) {

            var painter = painterResource(id = R.drawable.image_person)
            selectedImage.value?.let { uri ->
                if (uri != null){
                    painter = rememberAsyncImagePainter(uri)
                }
            }
            Image(
                    modifier = Modifier
                        .clip(CircleShape)
                        .height(152.dp)
                        .width(152.dp),
            painter = painter,
            contentDescription = "",
            contentScale = ContentScale.Crop
            )
            Image(
                modifier = Modifier
                    .clickable {
                        getContent.launch("image/*")
                    }
                    .clip(CircleShape)
                    .background(Main)
                    .height(32.dp)
                    .width(32.dp)
                    .padding(5.dp)
                    .align(alignment = Alignment.BottomEnd),
                painter = painterResource(id = R.drawable.camera),
                contentDescription = "",
                contentScale = ContentScale.Crop
            )
        }
    }
}




