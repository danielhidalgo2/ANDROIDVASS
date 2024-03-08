package com.chat.whatsvass

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class SplashView : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        window.statusBarColor = ContextCompat.getColor(this, R.color.light)
        super.onCreate(savedInstanceState)

        startActivity(Intent(this, LoginView::class.java))
    }
}