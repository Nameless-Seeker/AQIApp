package com.example.skeleton.Model.Repository

import android.content.Context
import androidx.datastore.preferences.core.byteArrayPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_session")

class DataStoreRepository(
    private val context: Context,
    private val tinkCryptoManager: TinkCryptoManager
) {


    // Store encrypted data as a ByteArray
    private val SECURE_ACCESS_TOKEN = byteArrayPreferencesKey("secure_access_token")
    private val SECURE_REFRESH_TOKEN = byteArrayPreferencesKey("secure_refresh_token")

    suspend fun saveSession(
        accessToken: String,
        refreshToken: String
    ) {

        val encryptedAccessToken = tinkCryptoManager.encrypt(accessToken)
        val encryptedRefreshToken = tinkCryptoManager.encrypt(refreshToken)

        context.dataStore.edit { prefs ->
            prefs[SECURE_ACCESS_TOKEN] = encryptedAccessToken
            prefs[SECURE_REFRESH_TOKEN] = encryptedRefreshToken
        }
    }

    fun getAccessToken(): Flow<String?> {
        return context.dataStore.data.map { prefs ->

            val encryptedBytes = prefs[SECURE_ACCESS_TOKEN]

            encryptedBytes?.let {
                try {
                    tinkCryptoManager.decrypt(it)
                } catch (e: Exception) {
                    null
                }
            }
        }
    }

    fun getRefreshToken(): Flow<String?> {
        return context.dataStore.data.map { prefs ->

            val encryptedBytes = prefs[SECURE_REFRESH_TOKEN]

            encryptedBytes?.let {
                try {
                    tinkCryptoManager.decrypt(it)
                } catch (e: Exception) {
                    null
                }
            }
        }
    }

    suspend fun clearUser() {
        context.dataStore.edit { prefs ->
            prefs.remove(SECURE_ACCESS_TOKEN)
            prefs.remove(SECURE_REFRESH_TOKEN)
        }
    }
}