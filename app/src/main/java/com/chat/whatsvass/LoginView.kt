package com.chat.whatsvass

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class LoginView : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Login()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Login(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .requiredWidth(width = 393.dp)
            .requiredHeight(height = 852.dp)
            .clip(shape = RoundedCornerShape(20.dp))
            .background(color = Color(0xff8091f2))
    ) {
        TextButton(
            onClick = { },
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            modifier = Modifier
                .align(alignment = Alignment.Center)
                .offset(x = (-0.5).dp,
                    y = 246.dp)
                .requiredWidth(width = 200.dp)
                .requiredHeight(height = 64.dp)
        ) {
            Box(
                modifier = Modifier
                    .requiredWidth(width = 200.dp)
                    .requiredHeight(height = 64.dp)
            ) {
                Text(
                    text = "Login",
                    color = Color(0xfff2f2f2),
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold),
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentHeight(align = Alignment.CenterVertically))
            }
        }
        TextField(
            value = "",
            onValueChange = {},
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color(0xfff2f2f2)),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 58.dp,
                    y = 481.dp)
                .requiredWidth(width = 276.dp)
                .requiredHeight(height = 64.dp))
        Text(
            text = "Crear Usuario",
            color = Color.White,
            textAlign = TextAlign.Center,
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 117.dp,
                    y = 785.dp)
                .requiredWidth(width = 151.dp)
                .requiredHeight(height = 45.dp)
                .wrapContentHeight(align = Alignment.CenterVertically))
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "logo 3",
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 87.dp,
                    y = 92.dp)
                .requiredSize(size = 218.dp))
        Text(
            text = "contraseña\n",
            color = Color(0xff274073),
            textAlign = TextAlign.Center,
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold),
            modifier = Modifier
                .align(alignment = Alignment.BottomStart)
                .offset(x = 58.dp,
                    y = (-307).dp)
                .requiredWidth(width = 276.dp)
                .requiredHeight(height = 64.dp)
                .wrapContentHeight(align = Alignment.Bottom))
        Text(
            text = "Usuario",
            color = Color(0xff274073),
            textAlign = TextAlign.Center,
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 58.dp,
                    y = 382.dp)
                .requiredWidth(width = 276.dp)
                .requiredHeight(height = 64.dp)
                .wrapContentHeight(align = Alignment.CenterVertically))
    }
}

@Preview(widthDp = 393, heightDp = 852)
@Composable
private fun LoginPreview() {
    Login(Modifier)
}