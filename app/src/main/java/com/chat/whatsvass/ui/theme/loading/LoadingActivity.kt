package com.chat.whatsvass.ui.theme.loading

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.chat.whatsvass.databinding.ActivityLoadingBinding
import com.chat.whatsvass.ui.theme.home.HomeView

class LoadingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoadingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoadingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val token = intent.getStringExtra("token")


        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, HomeView::class.java)
            intent.putExtra("token", token) // Agrega el extra al intent
            startActivity(intent)
            finish()
        }, 2000)
    }
}