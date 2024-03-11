package com.chat.whatsvass.ui.theme.login


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chat.whatsvass.R
import com.chat.whatsvass.ui.theme.Claro
import com.chat.whatsvass.ui.theme.Oscuro
import com.chat.whatsvass.ui.theme.loading.LoadingActivity

const val Shape = 20

class LoginView : ComponentActivity() {
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel = remember { LoginViewModel() }

            val username = remember { mutableStateOf("") }
            val password = remember { mutableStateOf("") }

            LoginScreen(viewModel, username.value, password.value) { newUser, newPassword ->
                username.value = newUser
                password.value = newPassword
            }
        }

        window.decorView.setOnTouchListener { _, _ ->
            hideKeyboard(this)
            false
        }
    }
}

fun hideKeyboard(activity: Activity) {
    val inputMethodManager =
        activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(activity.currentFocus?.windowToken, 0)
}

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    username: String,
    password: String,
    onCredentialsChange: (String, String) -> Unit
) {
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Claro)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(60.dp))
            Logo()
            Spacer(modifier = Modifier.height(60.dp))
            val textFieldModifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 70.dp)
                .height(60.dp)
            UserTextField(
                value = username,
                onValueChange = { onCredentialsChange(it, password) },
                modifier = textFieldModifier
            )
            Spacer(modifier = Modifier.height(40.dp))
            PasswordTextField(
                value = password,
                onValueChange = { onCredentialsChange(username, it) },
                modifier = textFieldModifier
            )

            Spacer(modifier = Modifier.height(90.dp))
            LoginButton(
                onClick = {
                    viewModel.loginUser(username, password)


                }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 95.dp)
                    .height(60.dp)
            )
            Spacer(modifier = Modifier.weight(0.3f))
            CreateAccountText()
            Spacer(modifier = Modifier.height(40.dp))

            // Mostrar mensaje de error si existe
            errorMessage?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }

    viewModel._loginResult.collectAsState().value?.let { result ->
        when (result) {
            is LoginViewModel.LoginResult.Error -> {
                errorMessage = "Los credenciales no son correctos"
            }
            else -> {
                val intent = Intent(context, LoadingActivity::class.java)
                context.startActivity(intent)
                errorMessage = null
            }
        }
    }
}


@Composable
fun Logo() {
    Image(
        painter = painterResource(id = R.drawable.logo),
        contentDescription = "Logo",
        modifier = Modifier.size(218.dp),
        contentScale = ContentScale.Fit
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { androidx.compose.material.Text("Ingrese su usuario") },
        shape = RoundedCornerShape(20.dp),
        singleLine = true,
        modifier = modifier,
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var passwordVisibility by remember { mutableStateOf(false) }
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { androidx.compose.material.Text("Ingrese su contraseña") },
        shape = RoundedCornerShape(Shape.dp),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = if (passwordVisibility) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        modifier = modifier,
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        trailingIcon = {
            IconButton(
                onClick = { passwordVisibility = !passwordVisibility },
                modifier = Modifier.padding(8.dp)
            ) {
                val icon = if (passwordVisibility) {
                    ImageVector.vectorResource(id = R.drawable.visible_off)
                } else {
                    ImageVector.vectorResource(id = R.drawable.visible_on)
                }
                Icon(icon, contentDescription = "Toggle Password Visibility")
            }
        }
    )
}

@Composable
fun LoginButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(Shape.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Oscuro),
    ) {
        Text(
            text = "Login",
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}


@Composable
fun CreateAccountText() {
    Text(
        text = "Crear usuario",
        color = Color.White,
        fontSize = 18.sp,
        modifier = Modifier.clickable {
            // Lógica para manejar el click en el texto "Crear usuario"
        }

    )
}


// Función auxiliar para mostrar mensajes en la aplicación
fun showMessage(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}
