package com.example.utanglog.screens.register

interface RegisterContract {
    interface View {
        fun getFullName(): String
        fun getEmail(): String
        fun getPassword(): String
        fun getConfirmPassword(): String
        fun showSuccess()
        fun showError(message: String)
        fun showPasswordMismatch()
        fun showPasswordWeak()
        fun navigateToLogin()
        fun clearPasswordFields()
    }

    interface Presenter {
        fun onRegisterClick()
        fun onLoginLinkClick()
    }
}