package com.chat.whatsvass.ui.theme.loading

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.chat.whatsvass.databinding.ActivityLoadingBinding
import com.chat.whatsvass.ui.theme.home.HomeView

class LoadingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoadingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoadingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, HomeView::class.java))
            finish()
        }, 2000)
    }
}