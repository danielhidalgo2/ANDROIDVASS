package com.chat.whatsvass.ui.theme.settings

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
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
import com.chat.whatsvass.commons.KEY_TOKEN
import com.chat.whatsvass.commons.SHARED_SETTINGS
import com.chat.whatsvass.commons.SHARED_TOKEN
import com.chat.whatsvass.databinding.ActivitySettingsViewBinding
import com.chat.whatsvass.ui.theme.login.LoginView
import kotlinx.coroutines.launch

class SettingsView : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsViewBinding
    private val viewModel by viewModels<SettingsViewModel>()
    private lateinit var sharedPreferencesSettings: SharedPreferences
    private lateinit var sharedPreferencesToken: SharedPreferences
    private var isNotificationsChecked = false
    private var isDarkModeChecked = false
    private var isBiometricChecked = false
    private var isLanguageChecked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this, R.color.main)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.light)

        binding = ActivitySettingsViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            finish() // Volver a la pantalla anterior cuando se presiona el botón de retroceso
        }

        sharedPreferencesSettings = getSharedPreferences(SHARED_SETTINGS, Context.MODE_PRIVATE)
        sharedPreferencesToken = getSharedPreferences(SHARED_TOKEN, Context.MODE_PRIVATE)
        val token = sharedPreferencesToken.getString(KEY_TOKEN, null)
        initVars(sharedPreferencesSettings)
        setUpSwitchs(sharedPreferencesSettings)

        binding.btnLogout.setOnClickListener {
            if (token != null) {
                setUpViewModel()
                viewModel.logoutUser(token)
            } else {
                Toast.makeText(this, "Error al cerrar sesión", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initVars(sharedPreferences: SharedPreferences) {
        isNotificationsChecked = sharedPreferences.getBoolean(KEY_NOTIFICATIONS, false)
        isDarkModeChecked = sharedPreferences.getBoolean(KEY_MODE, false)
        isBiometricChecked = sharedPreferences.getBoolean(KEY_BIOMETRIC, false)
        isLanguageChecked = sharedPreferences.getBoolean(KEY_LANGUAGE, false)

        binding.switchNotifications.isChecked = isNotificationsChecked
        binding.switchMode.isChecked = isDarkModeChecked
        binding.switchBiometric.isChecked = isBiometricChecked
        binding.switchLanguage.isChecked = isLanguageChecked

    }

    private fun setUpViewModel() {
        lifecycleScope.launch {
            viewModel.logoutResult.collect {
                if (it.toString() == "Success(logout=Logout successful)") {
                    sharedPreferencesToken.edit().putString(KEY_TOKEN, null).apply()
                    Toast.makeText(this@SettingsView, "Usuario desconectado", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@SettingsView, LoginView::class.java))
                    finish()
                }
                Log.d("Exitoso", it.toString())
            }
        }
    }

    private fun setUpSwitchs(sharedPreferences: SharedPreferences) {
        val edit = sharedPreferences.edit()

        isNotificationsChecked = sharedPreferences.getBoolean(KEY_NOTIFICATIONS, false)
        isDarkModeChecked = sharedPreferences.getBoolean(KEY_MODE, false)
        isBiometricChecked = sharedPreferences.getBoolean(KEY_BIOMETRIC, false)
        isLanguageChecked = sharedPreferences.getBoolean(KEY_LANGUAGE, false)

        binding.switchNotifications.isChecked = isNotificationsChecked
        binding.switchMode.isChecked = isDarkModeChecked
        binding.switchBiometric.isChecked = isBiometricChecked
        binding.switchLanguage.isChecked = isLanguageChecked

        binding.switchNotifications.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                edit.putBoolean(KEY_NOTIFICATIONS, true)
                edit.commit()
            } else {
                edit.putBoolean(KEY_NOTIFICATIONS, false)
                edit.commit()
            }
        }
        binding.switchMode.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                enableDarkMode()
                edit.putBoolean(KEY_MODE, true)
                edit.commit()
            } else {
                disableDarkMode()
                edit.putBoolean(KEY_MODE, false)
                edit.commit()
            }
        }
        binding.switchBiometric.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                edit.putBoolean(KEY_BIOMETRIC, true)
                edit.commit()
            } else {
                edit.putBoolean(KEY_BIOMETRIC, false)
                edit.commit()
            }
        }
        binding.switchLanguage.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                edit.putBoolean(KEY_LANGUAGE, true)
                edit.commit()
            } else {
                edit.putBoolean(KEY_LANGUAGE, false)
                edit.commit()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish() // Volver a la pantalla anterior cuando se presiona la flecha hacia atrás en el ActionBar
        return true
    }

    private fun enableDarkMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        delegate.applyDayNight()
    }

    private fun disableDarkMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        delegate.applyDayNight()
    }
}