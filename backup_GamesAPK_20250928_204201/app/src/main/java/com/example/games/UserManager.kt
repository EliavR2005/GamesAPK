package com.example.games

import android.content.Context
import android.content.SharedPreferences

class UserManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("users_prefs", Context.MODE_PRIVATE)

    fun register(email: String, password: String): Boolean {
        val key = "user_$email"
        return if (prefs.contains(key)) {
            false // usuario ya existe
        } else {
            prefs.edit().putString(key, password).apply()
            true
        }
    }

    fun login(email: String, password: String): Boolean {
        val key = "user_$email"
        val stored = prefs.getString(key, null)
        return stored != null && stored == password
    }
}
