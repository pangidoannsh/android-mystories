package com.pangidoannsh.mystories.data.local.preerences

import android.content.Context
import android.content.SharedPreferences

class UserPreferences(private val context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun getToken(): String? {
        return sharedPreferences.getString(KEY_TOKEN, null)
    }

    fun saveToken(token: String) {
        sharedPreferences.edit().putString(KEY_TOKEN, token).apply()
    }

    fun clearUser() {
        sharedPreferences.edit().remove(KEY_TOKEN).apply()
        sharedPreferences.edit().remove(USER_NAME).apply()
        sharedPreferences.edit().remove(USER_ID).apply()
    }

    fun getUserId(): String {
        return sharedPreferences.getString(USER_ID, null) ?: ""
    }

    fun saveUserId(name: String) {
        sharedPreferences.edit().putString(USER_ID, name).apply()
    }

    fun getUserName(): String {
        return sharedPreferences.getString(USER_NAME, null) ?: "undifined name"
    }

    fun saveUserName(name: String) {
        sharedPreferences.edit().putString(USER_NAME, name).apply()
    }

    companion object {
        private const val PREFS_NAME = "user_pref"
        private const val KEY_TOKEN = "token"
        private const val USER_NAME = "user_name"
        private const val USER_ID = "user_id"

        @Volatile
        private var INSTANCE: UserPreferences? = null

        fun getInstance(context: Context): UserPreferences {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: UserPreferences(context).also { INSTANCE = it }
            }
        }
    }
}


