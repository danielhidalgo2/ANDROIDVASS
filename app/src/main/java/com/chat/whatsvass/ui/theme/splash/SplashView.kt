package com.chat.whatsvass.ui.theme.splash

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.chat.whatsvass.R
import com.chat.whatsvass.commons.KEY_MODE
import com.chat.whatsvass.commons.KEY_TOKEN
import com.chat.whatsvass.commons.SHARED_SETTINGS
import com.chat.whatsvass.commons.SHARED_TOKEN
import com.chat.whatsvass.ui.theme.home.HomeView
import com.chat.whatsvass.ui.theme.loading.LoadingActivity
import com.chat.whatsvass.ui.theme.login.LoginView
import com.chat.whatsvass.ui.theme.profile.ProfileView
import com.chat.whatsvass.ui.theme.settings.SettingsView

class SplashView : AppCompatActivity() {
    private lateinit var sharedPreferencesSettings: SharedPreferences
    private lateinit var sharedPreferencesToken: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        sharedPreferencesSettings = getSharedPreferences(SHARED_SETTINGS, Context.MODE_PRIVATE)
        sharedPreferencesToken = getSharedPreferences(SHARED_TOKEN, Context.MODE_PRIVATE)

        val isDarkModeChecked = sharedPreferencesSettings.getBoolean(KEY_MODE, false)

        if (isDarkModeChecked) {
            enableDarkMode()
        } else {
            disableDarkMode()
        }

        val token = sharedPreferencesToken.getString(KEY_TOKEN, null)
        if (token.isNullOrEmpty()){
            splashScreen.setKeepOnScreenCondition { true }
            startActivity(Intent(this, LoginView::class.java))
            finish()
        } else {
            splashScreen.setKeepOnScreenCondition { true }
            startActivity(Intent(this, HomeView::class.java))
            finish()
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