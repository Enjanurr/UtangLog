package com.example.utanglog.screens.login

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.utanglog.app.MyApplication
import com.example.utanglog.data.UserInfo

class LoginModel(private val context: Context) {

    private val sharedPref: SharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

    fun doesAccountExist(email: String): Boolean {
        val registeredEmail = sharedPref.getString("email", "")

        Log.d("LoginModel", "Checking email: '$email'")
        Log.d("LoginModel", "Against registered: '$registeredEmail'")

        val exists = email.equals(registeredEmail, ignoreCase = true)
        Log.d("LoginModel", "Account exists: $exists")

        return exists
    }

    fun validateCredentials(email: String, password: String): Boolean {
        Log.d("LoginModel", "Validating credentials for: $email")

        if (email.isEmpty() || password.isEmpty()) {
            return false
        }

        // Get registered user credentials with default values (null safety)
        val registeredEmail = sharedPref.getString("email", "") ?: ""
        val registeredPassword = sharedPref.getString("password", "") ?: ""
        val registeredFullName = sharedPref.getString("fullName", "") ?: ""
        val registeredPhone = sharedPref.getString("phone", "") ?: ""

        Log.d("LoginModel", "Registered email: $registeredEmail")
        Log.d("LoginModel", "Input email: $email")
        Log.d("LoginModel", "Input password: $password")

        // Check against registered user
        if (email.equals(registeredEmail, ignoreCase = true) && password == registeredPassword) {
            Log.d("LoginModel", "Login success")
            val userInfo = UserInfo(
                fullName = registeredFullName,
                email = email,
                phone = registeredPhone
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