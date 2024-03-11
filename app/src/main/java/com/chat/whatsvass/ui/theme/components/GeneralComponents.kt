package com.chat.whatsvass.ui.theme.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
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
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chat.whatsvass.R
import com.chat.whatsvass.ui.theme.Oscuro
import com.chat.whatsvass.ui.theme.Principal
import com.chat.whatsvass.ui.theme.login.Shape


object GeneralComponents {
    @Composable
    fun NavigationBarCustom(text: String) {
        NavigationBar(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            containerColor = Principal
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier
                        .padding(start = 40.dp),
                    painter = painterResource(id = R.drawable.icon_arrow_back),
                    contentDescription = ""
                )
                Text(
                    modifier = Modifier
                        .padding(start = 70.dp),
                    textAlign = TextAlign.Center,
                    text = text,
                    fontSize = 22.sp,
                    color = Color.White
                )

            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TextFieldCustom(label: String) {
        var user by remember { mutableStateOf("") }
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 70.dp)
                .height(60.dp),
            value = user,
            onValueChange = { user = it },
            label = { androidx.compose.material.Text(label)}, // Usar androidx.compose.material.Text
            shape = RoundedCornerShape(20.dp),
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun PasswordTextFieldCustom(label: String) {
        var password by remember { mutableStateOf("") }
        var passwordVisibility by remember { mutableStateOf(false) }

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 70.dp)
                .height(60.dp),
            value = password,
            onValueChange = { password = it },
            label = { androidx.compose.material.Text(label) }, // Usar androidx.compose.material.Text
            shape = RoundedCornerShape(Shape.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = if (passwordVisibility) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
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
    fun ButtonCustom(onClick: () -> Unit, modifier: Modifier = Modifier, text: String) {
        Button(
            onClick = onClick,
            modifier = modifier,
            shape = RoundedCornerShape(Shape.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Oscuro),
        ) {
            Text(
                text = text,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }
    }
}
