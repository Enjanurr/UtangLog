package com.example.utanglog.screens.login

interface LoginContract {
    interface View {
        fun getEmail(): String
        fun getPassword(): String
        fun showSuccess()
        fun showError(message: String)
        fun showEmptyFieldsError()
        fun navigateToHome()
        fun navigateToRegister()
        fun clearPasswordField()
    }

    interface Presenter {
        fun onLoginClick()
        fun onRegisterClick()
    }
}