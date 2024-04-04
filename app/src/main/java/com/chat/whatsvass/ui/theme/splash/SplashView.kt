package com.chat.whatsvass.ui.theme.splash

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.chat.whatsvass.commons.CHECK_BOX
import com.chat.whatsvass.commons.KEY_PASSWORD
import com.chat.whatsvass.commons.KEY_USERNAME
import com.chat.whatsvass.commons.SHARED_SETTINGS
import com.chat.whatsvass.commons.SHARED_USER_DATA
import com.chat.whatsvass.ui.theme.home.HomeView
import com.chat.whatsvass.ui.theme.login.LoginView
import com.chat.whatsvass.ui.theme.login.LoginViewModel
import com.chat.whatsvass.usecases.checkinternet.isInternetActive
import com.chat.whatsvass.usecases.encrypt.Encrypt
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

const val DELAY_TO_GET_TOKEN = 2000L
const val DELAY_TO_DECRYPT_PASSWORD = 300L

class SplashView : AppCompatActivity() {
    private lateinit var sharedPreferencesSettings: SharedPreferences
    private lateinit var sharedPreferencesUserData: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        sharedPreferencesSettings = getSharedPreferences(SHARED_SETTINGS, Context.MODE_PRIVATE)
        sharedPreferencesUserData = getSharedPreferences(SHARED_USER_DATA, Context.MODE_PRIVATE)

        val isInternetActive = isInternetActive()

        setToken()

        val check = sharedPreferencesUserData.getBoolean(CHECK_BOX, false)
        if (!check || !isInternetActive) {
            splashScreen.setKeepOnScreenCondition { true }
            startActivity(Intent(this, LoginView::class.java))
            finish()
        } else {
            splashScreen.setKeepOnScreenCondition { true }
            @Suppress("DEPRECATION")
            android.os.Handler().postDelayed({
                startActivity(Intent(this, HomeView::class.java))
                finish()
            }, DELAY_TO_GET_TOKEN)

        }
    }

    private fun setToken() {
        // Si recordar contraseña esta activo, hacer login aquí para obtener token y guardarlo en el object
        val username = sharedPreferencesUserData.getString(KEY_USERNAME, null)
        val password = sharedPreferencesUserData.getString(KEY_PASSWORD, null)
        if (username != null && password != null) {
            MainScope().launch {
                async {
                    val passwordDecrypt = Encrypt().decryptPassword(password)
                    delay(DELAY_TO_DECRYPT_PASSWORD)
                    LoginViewModel(this@SplashView.application).loginUser(
                        username,
                        passwordDecrypt
                    )
                }.await()
            }
        }
    }
}