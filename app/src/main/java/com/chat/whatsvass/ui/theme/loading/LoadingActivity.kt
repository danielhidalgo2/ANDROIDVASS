package com.chat.whatsvass.ui.theme.loading

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.chat.whatsvass.R
import com.chat.whatsvass.databinding.ActivityLoadingBinding

class LoadingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoadingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoadingBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}