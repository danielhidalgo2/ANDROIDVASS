package com.chat.whatsvass.ui.theme.profile

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImagePainter.State.Empty.painter
import coil.compose.rememberAsyncImagePainter
import com.chat.whatsvass.R
import com.chat.whatsvass.ui.theme.Oscuro
import com.chat.whatsvass.ui.theme.Principal
import com.chat.whatsvass.ui.theme.components.GeneralComponents.ButtonCustom
import com.chat.whatsvass.ui.theme.components.GeneralComponents.NavigationBarCustom
import com.chat.whatsvass.ui.theme.components.GeneralComponents.PasswordTextFieldCustom
import com.chat.whatsvass.ui.theme.components.GeneralComponents.TextFieldCustom
import com.chat.whatsvass.ui.theme.login.hideKeyboard
import com.chat.whatsvass.ui.theme.login.showMessage


class ProfileView : ComponentActivity() {

    private var imageUri: Uri? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            ProfileScreen(ProfileViewModel(), navController)
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
                    val registerResponse =
                        (registerResult as ProfileViewModel.RegisterResult.Success).register
                    showMessage(
                        context,
                        "Usuario creado correctamente. Token: ${registerResponse.user.token}"
                    )
                    //  IR HACIA HOME

                }

                is ProfileViewModel.RegisterResult.Error -> {
                    val errorMessage =
                        (registerResult as ProfileViewModel.RegisterResult.Error).message
                    showMessage(context, "Error al crear usuario: $errorMessage")
                }

                else -> {}
            }
        }
    }

    @Composable
    fun ImageProfile() {
        val context = LocalContext.current
        var image by remember { mutableStateOf<Uri?>(null) }
        var isImagePressed by remember { mutableStateOf(false) }
        var isImageSelected = false

        if (isImagePressed) {
            /*   AlertDialogExample(
                   context,
                   "Tomar imagen desde",
                   "Selecciona una opción")
               {
                   isImagePressed = false
                   image = imageUri
               }*/
            chooseImage()
            isImagePressed = false
            isImageSelected = true
            image = imageUri
        }

        Box(
            modifier = Modifier
                .height(152.dp)
                .width(152.dp)
        ) {

            image = imageUri

            val painter = if (image != null) {
                rememberAsyncImagePainter(image)
            } else {
                painterResource(id = R.drawable.image_person)
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
                        isImagePressed = true
                    }
                    .clip(CircleShape)
                    .background(Principal)
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

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun AlertDialogImage(
        context: Context,
        onDismiss: () -> Unit
    ) {
        AlertDialog(onDismissRequest = onDismiss) {
            Surface(
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Seleccionar imagen desde")
                    Row(
                        modifier = Modifier
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {

                        Image(
                            painter = painterResource(id = R.drawable.icon_gallery),
                            contentDescription = "",
                            contentScale = ContentScale.Crop
                        )

                        Button(
                            modifier = Modifier
                                .padding(start = 16.dp),
                            onClick = {
                                //  onGalleryButtonClick(context)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Oscuro)
                        ) {
                            Text("Galería")
                        }
                    }

                    Row(
                        modifier = Modifier
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Image(
                            painter = painterResource(id = R.drawable.icon_camera),
                            contentDescription = "",
                            contentScale = ContentScale.Crop
                        )

                        Button(
                            modifier = Modifier
                                .padding(start = 16.dp),
                            onClick = {
                                //onCameraButtonClick(context )
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Oscuro)
                        ) {
                            Text("Cámara")
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun AlertDialogExample(
        context: Context,
        dialogTitle: String,
        dialogText: String,
        onDismiss: () -> Unit
    ) {
        AlertDialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(
                dismissOnClickOutside = true,
                dismissOnBackPress = true
            ),
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.icon_camera),
                    contentDescription = "Camera Icon"
                )
            },
            title = {
                Text(text = dialogTitle)
            },
            text = {
                Text(text = dialogText)
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        chooseImage()
                    }
                ) {
                    Text("Cámara")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        chooseImage()
                    }
                ) {
                    Text("Galería")
                }
            }
        )
    }

    private fun chooseImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        galleryLauncher.launch(intent)
    }

    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val image = result.data
            imageUri = image!!.data
        }
    }
}




