package com.chat.whatsvass

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat

class ProfileView : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
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

            NavigationBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                containerColor = ContextCompat.getColor(this, R.color.main)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        modifier = Modifier
                            .padding(start = 19.1.dp),
                        painter = painterResource(id = R.drawable.icon_arrow_back),
                        contentDescription = ""
                    )
                    Text(
                        modifier = Modifier
                            .width(264.15.dp)
                            .padding(end = 15.42.dp)
                            .padding(start = 12.32.dp),
                        textAlign = TextAlign.Center,
                        text = "Crear Perfil",
                        fontSize = 18.sp,
                        color = Color.White
                    )
                }
            }

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
    }
}
