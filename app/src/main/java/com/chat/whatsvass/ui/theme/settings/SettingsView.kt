package com.chat.whatsvass.ui.theme.settings

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.chat.whatsvass.R
import com.chat.whatsvass.commons.KEY_BIOMETRIC
import com.chat.whatsvass.commons.KEY_LANGUAGE
import com.chat.whatsvass.commons.KEY_MODE
import com.chat.whatsvass.commons.KEY_NOTIFICATIONS
import com.chat.whatsvass.commons.SHARED_SETTINGS
import com.chat.whatsvass.commons.SHARED_USER_DATA
import com.chat.whatsvass.commons.SOURCE_ID
import com.chat.whatsvass.commons.VIEW_FROM
import com.chat.whatsvass.databinding.ActivitySettingsViewBinding
import com.chat.whatsvass.ui.theme.contacts.ContactsView
import com.chat.whatsvass.ui.theme.home.HomeView
import com.chat.whatsvass.ui.theme.login.LoginView
import com.chat.whatsvass.usecases.token.Token
import kotlinx.coroutines.launch

@Suppress("DEPRECATION")
class SettingsView : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsViewBinding
    private val viewModel by viewModels<SettingsViewModel>()
    private lateinit var sharedPreferencesSettings: SharedPreferences
    private lateinit var sharedPreferencesUserData: SharedPreferences
    private var isNotificationsChecked = false
    private var isDarkModeChecked = false
    private var isBiometricChecked = false
    private var isLanguageChecked = false

    private var finalDarkMode = false
    private var initialDarkMode = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewFrom = intent.getStringExtra(VIEW_FROM)

        binding = ActivitySettingsViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferencesSettings = getSharedPreferences(SHARED_SETTINGS, Context.MODE_PRIVATE)
        val isDarkModeActive = sharedPreferencesSettings.getBoolean(KEY_MODE, false)

        // Si el modo oscuro cambia, se crea de nuevo la actividad anterior
        finalDarkMode = isDarkModeActive
        initialDarkMode = isDarkModeActive

        if (isDarkModeActive) {
            window.navigationBarColor = ContextCompat.getColor(this, R.color.dark)
            binding.main.setBackgroundColor(getColor(R.color.dark))
        } else {
            window.navigationBarColor = ContextCompat.getColor(this, R.color.light)
            binding.main.setBackgroundColor(getColor(R.color.light))
        }

        binding.btnBack.setOnClickListener {
            if (viewFrom == "Home" && (initialDarkMode != finalDarkMode)) {
                val intent = Intent(this, HomeView::class.java)
                startActivity(intent)
                finish()
            } else if (viewFrom == "Contacts" && (initialDarkMode != finalDarkMode)) {
                val intent = Intent(this, ContactsView::class.java)
                startActivity(intent)
                finish()
            } else {
                finish()
            }

        }

        sharedPreferencesUserData = getSharedPreferences(SHARED_USER_DATA, Context.MODE_PRIVATE)
        val token = Token.token
        initVars(sharedPreferencesSettings)
        setUpSwitchs(sharedPreferencesSettings)

        binding.btnLogout.setOnClickListener {
            if (token != null) {
                setUpViewModel()
                viewModel.logoutUser(token)
            } else {
                Toast.makeText(this, getString(R.string.failedToLogout), Toast.LENGTH_SHORT).show()
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

    }

    private fun setUpViewModel() {
        lifecycleScope.launch {
            viewModel.isLogoutFinishFlow.collect {
                if (it) {
                    viewModel.logoutResult.collect { result ->
                        if (result == "Logout successful") {
                            sharedPreferencesUserData.edit().putString(SOURCE_ID, null).apply()

                            Toast.makeText(
                                this@SettingsView,
                                getString(R.string.disconnectedUser), Toast.LENGTH_SHORT
                            ).show()
                            startActivity(Intent(this@SettingsView, LoginView::class.java))
                            finish()
                        } else if (result == null) {
                            Toast.makeText(
                                this@SettingsView,
                                getString(R.string.couldNotDisconnect), Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
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

        binding.switchNotifications.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                edit.putBoolean(KEY_NOTIFICATIONS, true)
                edit.apply()
            } else {
                edit.putBoolean(KEY_NOTIFICATIONS, false)
                edit.commit()
            }
        }
        binding.switchMode.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                edit.putBoolean(KEY_MODE, true)
                edit.commit()
                binding.main.setBackgroundColor(getColor(R.color.dark))
                window.navigationBarColor = ContextCompat.getColor(this, R.color.dark)
                finalDarkMode = true
            } else {
                edit.putBoolean(KEY_MODE, false)
                edit.commit()
                binding.main.setBackgroundColor(getColor(R.color.light))
                window.navigationBarColor = ContextCompat.getColor(this, R.color.light)
                finalDarkMode = false
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
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        val viewFrom = intent.getStringExtra(VIEW_FROM)
        if (viewFrom == "Home" && (initialDarkMode != finalDarkMode)) {
            val intent = Intent(this, HomeView::class.java)
            startActivity(intent)
            finish()
        } else if (viewFrom == "Contacts" && (initialDarkMode != finalDarkMode)) {
            val intent = Intent(this, ContactsView::class.java)
            startActivity(intent)
            finish()
        } else {
            finish()
        }
    }
}