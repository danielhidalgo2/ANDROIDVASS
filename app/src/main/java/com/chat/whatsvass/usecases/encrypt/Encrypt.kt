package com.chat.whatsvass.usecases.encrypt

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import android.util.Log
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

class Encrypt {
    private val TAG = "Encrypt"

    init {
        createKeyIfNecessary()
    }

    private fun createKeyIfNecessary() {
        try {
            val keyStore = KeyStore.getInstance("AndroidKeyStore")
            keyStore.load(null)
            if (!keyStore.containsAlias(KEY_ALIAS)) {
                val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
                val keyGenParameterSpec = KeyGenParameterSpec.Builder(
                    KEY_ALIAS,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .setUserAuthenticationRequired(false)
                    .build()
                keyGenerator.init(keyGenParameterSpec)
                keyGenerator.generateKey()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error creating key: ${e.message}")
        }
    }

    fun encryptPassword(input: String): String {
        try {
            val cipher = Cipher.getInstance(TRANSFORMATION)
            val keyStore = KeyStore.getInstance("AndroidKeyStore")
            keyStore.load(null)
            val secretKey = keyStore.getKey(KEY_ALIAS, null) as SecretKey

            cipher.init(Cipher.ENCRYPT_MODE, secretKey)
            val ivBytes = cipher.iv
            val encryptedBytes = cipher.doFinal(input.toByteArray())

            // Concatenar IV con los datos cifrados
            val combined = ByteArray(ivBytes.size + encryptedBytes.size)
            System.arraycopy(ivBytes, 0, combined, 0, ivBytes.size)
            System.arraycopy(encryptedBytes, 0, combined, ivBytes.size, encryptedBytes.size)

            return Base64.encodeToString(combined, Base64.DEFAULT)
        } catch (e: Exception) {
            Log.e(TAG, "Error encrypting password: ${e.message}")
            throw RuntimeException("Error encrypting password: ${e.message}", e)
        }
    }

    fun decryptPassword(encryptedPassword: String): String {
        try {
            val cipher = Cipher.getInstance(TRANSFORMATION)
            val keyStore = KeyStore.getInstance("AndroidKeyStore")
            keyStore.load(null)
            val secretKey = keyStore.getKey(KEY_ALIAS, null) as SecretKey

            // Decodificar el texto cifrado y el IV
            val combined = Base64.decode(encryptedPassword, Base64.DEFAULT)
            val ivBytes = combined.copyOfRange(0, cipher.blockSize)
            val encryptedBytes = combined.copyOfRange(cipher.blockSize, combined.size)
            val ivParams = IvParameterSpec(ivBytes)

            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParams)
            val decryptedBytes = cipher.doFinal(encryptedBytes)
            return String(decryptedBytes)
        } catch (e: Exception) {
            Log.e(TAG, "Error decrypting password: ${e.message}")
            throw RuntimeException("Error decrypting password: ${e.message}", e)
        }
    }

    companion object {
        private const val KEY_ALIAS = "WhatsVass3lm3j0r"
        private const val TRANSFORMATION = "AES/CBC/PKCS7Padding"
    }
}
