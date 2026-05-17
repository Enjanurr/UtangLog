package com.example.utanglog.screens.register

import android.content.Context
import android.content.SharedPreferences

class RegisterModel(private val context: Context) {

    private val sharedPref: SharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

    fun validateInput(
        fullName: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        return fullName.isNotEmpty() &&
                email.isNotEmpty() &&
                email.contains("@") &&
                password.isNotEmpty() &&
                password == confirmPassword &&
                password.length >= 6
    }

    fun saveUser(fullName: String, email: String, password: String): Boolean {
        return try {
            sharedPref.edit().apply {
                putString("fullName", fullName)
                putString("email", email)
                putString("password", password)
                putBoolean("isLoggedIn", false)
                apply()
            }.commit()
        } catch (e: Exception) {
            false
        }
    }

    fun isEmailExists(email: String): Boolean {
        val savedEmail = sharedPref.getString("email", "")
        return email.equals(savedEmail, ignoreCase = true)
    }
}