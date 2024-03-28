package com.chat.whatsvass.ui.theme.profile

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.chat.whatsvass.R
import com.chat.whatsvass.commons.DELAY_TO_ENCRYPT
import com.chat.whatsvass.commons.KEY_PASSWORD
import com.chat.whatsvass.commons.KEY_USERNAME
import com.chat.whatsvass.ui.theme.Dark
import com.chat.whatsvass.ui.theme.Light
import com.chat.whatsvass.ui.theme.Main
import com.chat.whatsvass.ui.theme.components.GeneralComponents.ButtonCustom
import com.chat.whatsvass.ui.theme.components.GeneralComponents.NavigationBarCustom
import com.chat.whatsvass.ui.theme.components.GeneralComponents.PasswordTextFieldCustom
import com.chat.whatsvass.ui.theme.components.GeneralComponents.TextFieldCustom
import com.chat.whatsvass.ui.theme.home.HomeView
import com.chat.whatsvass.ui.theme.login.hideKeyboard
import com.chat.whatsvass.ui.theme.login.showMessage
import com.chat.whatsvass.usecases.encrypt.Encrypt
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ProfileView : ComponentActivity() {

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = ContextCompat.getColor(this, R.color.main)

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
    fun ProfileScreen(
        viewModel: ProfileViewModel,
        navController: NavController,
        isDarkModeActive: Boolean,
        sharedPreferences: SharedPreferences
    ) {

        val context = LocalContext.current
        val registerResult by viewModel.registerResult.collectAsState()
        val keyboardController = LocalSoftwareKeyboardController.current

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(if (isDarkModeActive) Dark else Light)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                NavigationBarCustom(
                    text = stringResource(R.string.createProfile),
                    onBackClick = { navController.popBackStack() })
                Spacer(modifier = Modifier.height(20.dp))
                ImageProfile()
                Spacer(modifier = Modifier.height(40.dp))
                val user = TextFieldCustom(
                    stringResource(R.string.user),
                    isDarkModeActive,
                    onImeActionPerformed = { action ->
                        if (action == ImeAction.Done || action == ImeAction.Next) {
                            keyboardController?.hide()
                        }
                    },
                )
                Spacer(modifier = Modifier.height(20.dp))
                val nick = TextFieldCustom(
                    stringResource(R.string.nick),
                    isDarkModeActive,
                    onImeActionPerformed = { action ->
                        if (action == ImeAction.Done || action == ImeAction.Next) {
                            keyboardController?.hide()
                        }
                    },
                )
                Spacer(modifier = Modifier.height(20.dp))
                val password = PasswordTextFieldCustom(
                    stringResource(R.string.password),
                    isDarkModeActive,
                    onImeActionPerformed = { action ->
                        if (action == ImeAction.Done || action == ImeAction.Next) {
                            keyboardController?.hide()
                        }
                    },
                )
                Spacer(modifier = Modifier.height(20.dp))
                val confirmPassword = PasswordTextFieldCustom(
                    stringResource(R.string.repeatPassword),
                    isDarkModeActive,
                    onImeActionPerformed = { action ->
                        if (action == ImeAction.Done || action == ImeAction.Next) {
                            keyboardController?.hide()
                        }
                    },
                )
                Spacer(modifier = Modifier.weight(0.3f))

                ButtonCustom(
                    onClick = {
                        if (user.isNullOrEmpty()) {
                            Toast.makeText(
                                context, R.string.enterYourUsername,
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        } else if (nick.isNullOrEmpty()) {
                            Toast.makeText(context, R.string.enterYourNick, Toast.LENGTH_SHORT)
                                .show()
                        } else if (password.isNullOrEmpty()) {
                            Toast.makeText(context, R.string.enterAPassword, Toast.LENGTH_SHORT)
                                .show()
                        } else if (confirmPassword.isNullOrEmpty()) {
                            Toast.makeText(
                                context,
                                R.string.confirmYourPassword,
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        } else if (password != confirmPassword) {
                            Toast.makeText(
                                context, R.string.passwordsMustBeTheSame,
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        } else {
                            viewModel.registerUser(user, password, nick)
                            CoroutineScope(Dispatchers.Main).launch {
                                val encryptPassword = Encrypt().encryptPassword(password)
                                delay(DELAY_TO_ENCRYPT)
                                if (registerResult == "success") {
                                    Toast.makeText(
                                        context, R.string.userCreatedSuccessfully,
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()

                                    sharedPreferences.edit()
                                        .putString(KEY_PASSWORD, encryptPassword).apply()
                                    sharedPreferences.edit().putString(KEY_USERNAME, user).apply()

                                    val intent = Intent(context, HomeView::class.java)
                                    context.startActivity(intent)
                                } else if (registerResult == "failed") {
                                    Toast.makeText(
                                        context, R.string.failedToCreateUser,
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 95.dp)
                        .height(60.dp),
                    text = stringResource(R.string.createUser)
                )
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }


    @Composable
    fun ImageProfile() {
        val selectedImage = remember { mutableStateOf<Uri?>(null) }

        // Abrir galeria
        val getContent =
            rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
                selectedImage.value = uri
            }

        Box(
            modifier = Modifier
                .height(152.dp)
                .width(152.dp)
        ) {

            var painter = painterResource(id = R.drawable.image_person)
            selectedImage.value?.let { uri ->
                if (uri != null) {
                    painter = rememberAsyncImagePainter(uri)
                }
            }
            Image(
                modifier = Modifier
                    .clip(CircleShape)
                    .height(152.dp)
                    .width(152.dp)
                    .background(Color.LightGray),
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




