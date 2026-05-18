package com.example.utanglog.screens.profile

import android.content.Context
import android.content.SharedPreferences
import com.example.utanglog.app.MyApplication
import com.example.utanglog.data.UserInfo

class ProfileModel(private val context: Context) {

    private val sharedPref: SharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

    fun getUser(): UserInfo {
        // First try to get from Application class
        val appUser = MyApplication.getInstance().getUserInfo()
        if (appUser != null && appUser.fullName.isNotEmpty()) {
            return appUser
        }

        // Fallback to SharedPreferences
        return UserInfo(
            fullName = sharedPref.getString("fullName", "John Doe") ?: "John Doe",
            email = sharedPref.getString("email", "john.doe@example.com") ?: "john.doe@example.com",
            phone = sharedPref.getString("phone", "+63 912 345 6789") ?: "+63 912 345 6789",
            profileImagePath = sharedPref.getString("profileImagePath", "") ?: ""
        )
    }

    fun updateUser(name: String, email: String, phone: String): Boolean {
        return if (name.isNotEmpty() && email.isNotEmpty()) {
            // Update SharedPreferences
            sharedPref.edit().apply {
                putString("fullName", name)
                putString("email", email)
                putString("phone", phone)
                apply()
            }.commit()

            // Update Application class
            val currentUser = MyApplication.getInstance().getUserInfo() ?: UserInfo()
            val updatedUser = UserInfo(
                fullName = name,
                email = email,
                phone = phone,
                profileImagePath = currentUser.profileImagePath
            )
            MyApplication.getInstance().setUserInfo(updatedUser)

            true
        } else {
            false
        }
    }

    fun saveProfileImagePath(imagePath: String): Boolean {
        // Update SharedPreferences
        sharedPref.edit().apply {
            putString("profileImagePath", imagePath)
            apply()
        }.commit()

        // Update Application class
        val currentUser = MyApplication.getInstance().getUserInfo() ?: UserInfo()
        val updatedUser = currentUser.copy(profileImagePath = imagePath)
        MyApplication.getInstance().setUserInfo(updatedUser)

        return true
    }

    fun getLoggedInEmail(): String {
        val appUser = MyApplication.getInstance().getUserInfo()
        if (appUser != null && appUser.email.isNotEmpty()) {
            return appUser.email
        }
        return sharedPref.getString("loggedInEmail", "") ?: ""
    }

    fun logout(): Boolean {
        MyApplication.getInstance().clearUserInfo()
        return sharedPref.edit().apply {
            putBoolean("isLoggedIn", false)
            remove("loggedInEmail")
            apply()
        }.commit()
    }
}