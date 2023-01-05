package com.example.wakemeup.auth

import android.content.Context
import android.content.SharedPreferences

class preferAuth(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("auth", Context.MODE_PRIVATE)

    fun getId(key: String): String {
        return prefs.getString(key, "").toString()
    }

    fun setId(key: String, str: String) {
        prefs.edit().putString(key, str).apply()
    }
}