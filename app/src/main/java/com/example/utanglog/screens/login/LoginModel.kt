package com.example.utanglog.screens.login

import android.content.Context
import android.content.SharedPreferences

class LoginModel(private val context: Context) {

    private val sharedPref: SharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

    fun validateCredentials(email: String, password: String): Boolean {
        if (email.isEmpty() || password.isEmpty()) {
            return false
        }

        // Check for default user (for testing)
        if (email.equals("test@example.com", ignoreCase = true) && password == "test123") {
            return true
        }

        val savedEmail = sharedPref.getString("email", "")
        val savedPassword = sharedPref.getString("password", "")

        return email.equals(savedEmail, ignoreCase = true) && password == savedPassword
    }

    fun isUserLoggedIn(): Boolean {
        return sharedPref.getBoolean("isLoggedIn", false)
    }

    fun saveLoginSession(email: String) {
        sharedPref.edit().apply {
            putBoolean("isLoggedIn", true)
            putString("loggedInEmail", email)
            apply()
        }
    }

    fun clearLoginSession() {
        sharedPref.edit().apply {
            putBoolean("isLoggedIn", false)
            remove("loggedInEmail")
            apply()
        }
    }
}