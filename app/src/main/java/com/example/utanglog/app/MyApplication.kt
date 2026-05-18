package com.example.utanglog.app  // Change to your package name

import android.app.Application
import android.util.Log
import com.example.utanglog.data.UserInfo

class MyApplication : Application() {

    companion object {
        private lateinit var instance: MyApplication

        fun getInstance(): MyApplication = instance
    }

    private var userInfo: UserInfo? = null

    override fun onCreate() {
        super.onCreate()
        instance = this
        Log.d("MyApplication", "Application initialized")
    }

    fun setUserInfo(userInfo: UserInfo) {
        this.userInfo = userInfo
    }

    fun getUserInfo(): UserInfo? = userInfo

    fun isLoggedIn(): Boolean = userInfo != null

    fun clearUserInfo() {
        userInfo = null
    }
}