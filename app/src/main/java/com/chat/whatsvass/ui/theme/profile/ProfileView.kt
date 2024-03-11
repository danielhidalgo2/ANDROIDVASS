package com.chat.whatsvass.ui.theme.profile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.chat.whatsvass.R
import com.chat.whatsvass.ui.theme.components.GeneralComponents
import com.chat.whatsvass.ui.theme.components.GeneralComponents.NavigationBarCustom

class ProfileView : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        window.statusBarColor = ContextCompat.getColor(this, R.color.main)
        super.onCreate(savedInstanceState)
        setContent {
            ProfileScreen()
        }
    }
}
@Composable
fun ProfileScreen() {
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

            NavigationBarCustom(text = "Crear Perfil")
            Spacer(modifier = Modifier.height(40.dp))
            ImageProfile()
            Spacer(modifier = Modifier.height(40.dp))
            GeneralComponents.TextFieldCustom("Usuario")
            Spacer(modifier = Modifier.height(20.dp))
            GeneralComponents.TextFieldCustom("Nick")
            Spacer(modifier = Modifier.height(20.dp))
            GeneralComponents.PasswordTextFieldCustom("Contraseña")
            Spacer(modifier = Modifier.height(20.dp))
            GeneralComponents.PasswordTextFieldCustom("Repetir Contraseña")
            Spacer(modifier = Modifier.height(130.dp))
            GeneralComponents.ButtonCustom(onClick = {  },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 95.dp)
                    .height(60.dp),
                text = "Crear Usuario")
        }
    }
}
@Composable
fun ImageProfile() {
    Box(
        modifier = Modifier
            .height(152.dp)
            .width(152.dp)
    ) {
        Image(
            modifier = Modifier
                .clip(CircleShape)
                .shadow(2.dp)
                .height(152.dp)
                .width(152.dp)
                .padding(top = 24.dp),
            painter = painterResource(id = R.drawable.image_person),
            contentDescription = "",
            contentScale = ContentScale.Crop
        )

        Image(
            modifier = Modifier
                .height(32.dp)
                .width(32.dp)
                .align(alignment = Alignment.BottomEnd),
            painter = painterResource(id = R.drawable.icon_image),
            contentDescription = "",
            contentScale = ContentScale.Crop
        )
    }
}