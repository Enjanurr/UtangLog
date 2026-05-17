package com.example.utanglog.screens.profile

import android.content.Context
import android.content.SharedPreferences

data class User(
    var name: String = "John Doe",
    var email: String = "john.doe@example.com",
    var phone: String = "+63 912 345 6789",
    var profileImagePath: String = ""
)

class ProfileModel(private val context: Context) {

    private val sharedPref: SharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

    fun getUser(): User {
        return User(
            name = sharedPref.getString("fullName", "John Doe") ?: "John Doe",
            email = sharedPref.getString("email", "john.doe@example.com") ?: "john.doe@example.com",
            phone = sharedPref.getString("phone", "+63 912 345 6789") ?: "+63 912 345 6789",
            profileImagePath = sharedPref.getString("profileImagePath", "") ?: ""
        )
    }

    fun updateUser(name: String, email: String, phone: String): Boolean {
        return if (name.isNotEmpty() && email.isNotEmpty()) {
            sharedPref.edit().apply {
                putString("fullName", name)
                putString("email", email)
                putString("phone", phone)
                apply()
            }.commit()
        } else {
            false
        }
    }

    fun saveProfileImagePath(imagePath: String): Boolean {
        return sharedPref.edit().apply {
            putString("profileImagePath", imagePath)
            apply()
        }.commit()
    }

    fun getLoggedInEmail(): String {
        return sharedPref.getString("loggedInEmail", "") ?: ""
    }

    // ADD LOGOUT METHOD
    fun logout(): Boolean {
        return try {
            sharedPref.edit().apply {
                putBoolean("isLoggedIn", false)
                remove("loggedInEmail")
                apply()
            }.commit()
        } catch (e: Exception) {
            false
        }
    }
}