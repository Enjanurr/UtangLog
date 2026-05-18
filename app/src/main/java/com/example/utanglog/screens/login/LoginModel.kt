package com.example.utanglog.screens.login

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.utanglog.app.MyApplication
import com.example.utanglog.data.UserInfo

class LoginModel(private val context: Context) {

    private val sharedPref: SharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

    fun doesAccountExist(email: String): Boolean {
        val savedEmail = sharedPref.getString("email", "")
        Log.d("LoginModel", "Checking email: $email against saved: $savedEmail")
        return email.equals(savedEmail, ignoreCase = true)
    }

    fun validateCredentials(email: String, password: String): Boolean {
        if (email.isEmpty() || password.isEmpty()) {
            return false
        }

        // Check for default test user
        if (email.equals("test@example.com", ignoreCase = true) && password == "test123") {
            Log.d("LoginModel", "Default user login success")
            val userInfo = UserInfo(
                fullName = "Test User",
                email = email,
                phone = "+63 912 345 6789"
            )
            MyApplication.getInstance().setUserInfo(userInfo)
            return true
        }

        val savedEmail = sharedPref.getString("email", "")
        val savedPassword = sharedPref.getString("password", "")
        val savedFullName = sharedPref.getString("fullName", "")
        val savedPhone = sharedPref.getString("phone", "")

        Log.d("LoginModel", "Comparing: email=$email vs savedEmail=$savedEmail")
        Log.d("LoginModel", "Comparing: password=$password vs savedPassword=$savedPassword")

        if (email.equals(savedEmail, ignoreCase = true) && password == savedPassword) {
            Log.d("LoginModel", "User login success")
            val userInfo = UserInfo(
                fullName = savedFullName ?: "",
                email = email,
                phone = savedPhone ?: ""
            )
            MyApplication.getInstance().setUserInfo(userInfo)
            return true
        }

        Log.d("LoginModel", "Login failed")
        return false
    }

    fun isUserLoggedIn(): Boolean {
        return sharedPref.getBoolean("isLoggedIn", false) || MyApplication.getInstance().isLoggedIn()
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
        MyApplication.getInstance().clearUserInfo()
    }
}