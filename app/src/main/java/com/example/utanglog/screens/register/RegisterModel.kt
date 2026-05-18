package com.example.utanglog.screens.register

import android.content.Context
import android.content.SharedPreferences
import com.example.utanglog.data.UserInfo

class RegisterModel(private val context: Context) {

    private val sharedPref: SharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

    init {
        // Only create default user if NO users exist at all
        createDefaultUserIfNoUsers()
    }

    private fun createDefaultUserIfNoUsers() {
        val hasAnyUser = sharedPref.getBoolean("hasAnyUser", false)

        if (!hasAnyUser) {
            sharedPref.edit().apply {
                putString("fullName", "Test User")
                putString("email", "test@example.com")
                putString("password", "test123")
                putString("phone", "+63 912 345 6789")
                putBoolean("defaultUserCreated", true)
                putBoolean("hasAnyUser", true)  // Mark that we have a user
                apply()
            }
        }
    }

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
                putString("phone", "")
                putBoolean("isLoggedIn", false)
                putBoolean("hasAnyUser", true)  // Mark that we have a user
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