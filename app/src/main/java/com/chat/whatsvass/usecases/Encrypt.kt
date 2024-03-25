package com.chat.whatsvass.usecases

import com.chat.whatsvass.BuildConfig
import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

class Encrypt {
    fun encryptPassword(input: String): String {
        try {
            val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
            val secretKeySpec = SecretKeySpec(BuildConfig.CLAVE_SECRETA.toByteArray(), "AES")
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec)
            val encryptedBytes = cipher.doFinal(input.toByteArray())
            return Base64.encodeToString(encryptedBytes, Base64.DEFAULT)
        } catch (e: Exception) {
            // Manejar cualquier excepción que pueda ocurrir durante el proceso de encriptación
            throw RuntimeException("Error al encriptar la contraseña: ${e.message}", e)
        }
    }

    fun decryptPassword(encryptedPassword: String): String {
        try {
            val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
            val secretKeySpec = SecretKeySpec(BuildConfig.CLAVE_SECRETA.toByteArray(), "AES")
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec)
            val decryptedBytes = cipher.doFinal(Base64.decode(encryptedPassword, Base64.DEFAULT))
            return String(decryptedBytes)
        } catch (e: Exception) {
            // Manejar cualquier excepción que pueda ocurrir durante el proceso de desencriptación
            throw RuntimeException("Error al desencriptar la contraseña: ${e.message}", e)
        }
    }
}