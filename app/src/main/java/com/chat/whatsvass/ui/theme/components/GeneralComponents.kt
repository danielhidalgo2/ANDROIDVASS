package com.chat.whatsvass.ui.theme.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AppBarDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chat.whatsvass.R
import com.chat.whatsvass.ui.theme.Dark
import com.chat.whatsvass.ui.theme.DarkMode
import com.chat.whatsvass.ui.theme.Main
import com.chat.whatsvass.ui.theme.White
import com.chat.whatsvass.ui.theme.login.Shape

object GeneralComponents {
    @Composable
    fun NavigationBarCustom(text: String, onBackClick: () -> Unit) {
        TopAppBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            backgroundColor = Main,
            elevation = AppBarDefaults.TopAppBarElevation,
            title = {
                IconButton(
                    modifier = Modifier
                        .padding(start = 30.dp),
                    onClick = onBackClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_arrow_back),
                        contentDescription = stringResource(R.string.back),
                        tint = Dark
                    )
                }
                Row(
                    modifier = Modifier
                        .padding(end = 90.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = text,
                        fontSize = 22.sp,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                }
            }
        )
    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TextFieldCustom(label: String, isDarkModeActive: Boolean, onImeActionPerformed: (ImeAction) -> Unit): String {
        var data by remember { mutableStateOf("") }
        val colorText = if (isDarkModeActive) White else Color.Black

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 70.dp)
                .height(60.dp),
            value = data,
            onValueChange = { data = it },
            label = { androidx.compose.material.Text(
                text = label,
                color =  if (isDarkModeActive) White else Color.Black,
                fontSize = 14.sp
            )},
            shape = RoundedCornerShape(20.dp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onDone = { onImeActionPerformed(ImeAction.Next) }),
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                containerColor = if (isDarkModeActive) DarkMode else White,
                cursorColor = colorText,
                focusedTextColor = colorText,
                unfocusedTextColor = colorText
            )
        )
        return data
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun PasswordTextFieldCustom(label: String, isDarkModeActive: Boolean, onImeActionPerformed: (ImeAction) -> Unit): String {
        var password by remember { mutableStateOf("") }
        var passwordVisibility by remember { mutableStateOf(false) }
        val colorText = if (isDarkModeActive) White else Color.Black

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 70.dp)
                .height(60.dp),
            value = password,
            onValueChange = { password = it },
            label = { androidx.compose.material.Text(
                text = label,
                color =  if (isDarkModeActive) White else Color.Black,
                fontSize = 14.sp
            ) }, // Usar androidx.compose.material.Text
            shape = RoundedCornerShape(Shape.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onDone = { onImeActionPerformed(ImeAction.Next) }),
            visualTransformation = if (passwordVisibility) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                containerColor = if (isDarkModeActive) DarkMode else White,
                cursorColor = colorText,
                focusedTextColor = colorText,
                unfocusedTextColor = colorText
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
                    Icon(icon, contentDescription = stringResource(R.string.togglePasswordVisibility), tint = if (isDarkModeActive) White else Color.Black)
                }
            }
        )
        return password
    }

    @Composable
    fun ButtonCustom(onClick: () -> Unit, modifier: Modifier = Modifier, text: String) {
        Button(
            onClick = onClick,
            modifier = modifier,
            shape = RoundedCornerShape(Shape.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Main),
        ) {
            Text(
                text = text,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }
    }
}
