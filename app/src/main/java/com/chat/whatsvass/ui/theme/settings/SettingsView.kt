package com.chat.whatsvass.ui.theme.settings

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.chat.whatsvass.R
import com.chat.whatsvass.databinding.ActivitySettingsViewBinding
import com.chat.whatsvass.ui.theme.Oscuro

class SettingsView : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this, R.color.main)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.light)

        binding = ActivitySettingsViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        binding.switchNotifications.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {

            } else {

            }
        }
        binding.switchMode.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {

            } else {

            }
        }
        binding.switchBiometric.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {

            } else {

            }
        }
        binding.switchLanguage.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {

            } else {

            }
        }

    }
}