package com.eldar.eldarwallet.util
import android.util.Base64
import java.security.Key
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import kotlin.experimental.xor
object EncryptionUtils {

    private const val ALGORITHM = "AES"
    private const val TRANSFORMATION = "AES"

    fun encrypt(input: String, key: String): String {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, generateKey(key))
        val encryptedBytes = cipher.doFinal(input.toByteArray())
        return Base64.encodeToString(encryptedBytes, Base64.DEFAULT)
    }

    fun decrypt(input: String, key: String): String {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.DECRYPT_MODE, generateKey(key))
        val decodedBytes = Base64.decode(input, Base64.DEFAULT)
        val decryptedBytes = cipher.doFinal(decodedBytes)
        return String(decryptedBytes)
    }

    private fun generateKey(key: String): Key {
        val keyBytes = key.toByteArray()
        val keyLength = 16
        val keyBytesPadded = ByteArray(keyLength)

        for (i in keyBytes.indices) {
            keyBytesPadded[i % keyLength] = (keyBytesPadded[i % keyLength] xor keyBytes[i])
        }

        return SecretKeySpec(keyBytesPadded, ALGORITHM)
    }
}