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
import com.chat.whatsvass.commons.KEY_NOTIFICATIONS
import com.chat.whatsvass.commons.KEY_TOKEN
import com.chat.whatsvass.commons.SHARED_SETTINGS
import com.chat.whatsvass.commons.SHARED_USER_DATA
import com.chat.whatsvass.ui.theme.home.HomeView
import com.chat.whatsvass.ui.theme.login.LoginView
import com.chat.whatsvass.usecases.firebase.App

class SplashView : AppCompatActivity() {
    private lateinit var sharedPreferencesSettings: SharedPreferences
    private lateinit var sharedPreferencesUserData: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        sharedPreferencesSettings = getSharedPreferences(SHARED_SETTINGS, Context.MODE_PRIVATE)
        sharedPreferencesUserData = getSharedPreferences(SHARED_USER_DATA, Context.MODE_PRIVATE)

        val isNotificationsActive = sharedPreferencesSettings.getBoolean(KEY_NOTIFICATIONS, false)

        if (isNotificationsActive){

        }
        val check = sharedPreferencesUserData.getBoolean(CHECK_BOX, false)
        if (!check){
            splashScreen.setKeepOnScreenCondition { true }
            startActivity(Intent(this, LoginView::class.java))
            finish()
        } else {
            splashScreen.setKeepOnScreenCondition { true }
            Handler().postDelayed({
                startActivity(Intent(this, HomeView::class.java))
                finish()
            }, 2000)
        }
    }

    private fun setToken() {
        // Si recordar contraseña esta activo, hacer login aquí para obtener token y guardarlo en el object
        val username = sharedPreferencesUserData.getString(KEY_USERNAME, null)
        val password = sharedPreferencesUserData.getString(KEY_PASSWORD, null)
        if (username != null && password != null) {
            MainScope().launch {
                async {
                    val passwordDecrypt = Encrypt().decryptPassword(password!!)
                    delay(300)
                    LoginViewModel(this@SplashView.application).loginUser(
                        username!!,
                        passwordDecrypt
                    )
                }.await()
            }
        }
    }
}