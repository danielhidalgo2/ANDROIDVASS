package com.chat.whatsvass.ui.theme.settings

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.chat.whatsvass.R
import com.chat.whatsvass.commons.KEY_BIOMETRIC
import com.chat.whatsvass.commons.KEY_LANGUAGE
import com.chat.whatsvass.commons.KEY_MODE
import com.chat.whatsvass.commons.KEY_NOTIFICATIONS
import com.chat.whatsvass.commons.SHARED_SETTINGS
import com.chat.whatsvass.databinding.ActivitySettingsViewBinding
import kotlinx.coroutines.launch

class SettingsView : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsViewBinding
    private val viewModel by viewModels<SettingsViewModel>()
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this, R.color.main)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.light)

        binding = ActivitySettingsViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences(SHARED_SETTINGS, Context.MODE_PRIVATE)
        val edit = sharedPreferences.edit()

        binding.switchNotifications.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                edit.putBoolean(KEY_NOTIFICATIONS, true)
            } else {
                edit.putBoolean(KEY_NOTIFICATIONS, false)
            }
        }
        binding.switchMode.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                enableDarkMode()
                edit.putBoolean(KEY_MODE, true)
            } else {
                disableDarkMode()
                edit.putBoolean(KEY_MODE, false)
            }
        }
        binding.switchBiometric.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                edit.putBoolean(KEY_BIOMETRIC, true)
            } else {
                edit.putBoolean(KEY_BIOMETRIC, false)
            }
        }
        binding.switchLanguage.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                edit.putBoolean(KEY_LANGUAGE, true)
            } else {
                edit.putBoolean(KEY_LANGUAGE, false)
            }
        }

        binding.btnLogout.setOnClickListener {
            var token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjE5MyIsImlhdCI6MTcxMDI0MDYzMiwiZXhwIjoxNzEyODMyNjMyfQ.dE6jWNrfOa5SvpMEcrfZDB_MICJqY6he9bA9g0g6nsQ"
            viewModel.logoutUser(token)
            setUpViewModel()
        }

    }

    private fun setUpViewModel(){
        lifecycleScope.launch {
            viewModel.logoutResult.collect {
                Toast.makeText(this@SettingsView, it.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun enableDarkMode(){
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        delegate.applyDayNight()
    }

    private fun disableDarkMode(){
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        delegate.applyDayNight()
    }
}