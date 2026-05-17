package com.example.utanglog.screens.register

class RegisterPresenter(
    private val view: RegisterContract.View,
    private val model: RegisterModel
) : RegisterContract.Presenter {

    override fun onRegisterClick() {
        val fullName = view.getFullName()
        val email = view.getEmail()
        val password = view.getPassword()
        val confirmPassword = view.getConfirmPassword()

        if (password != confirmPassword) {
            view.showPasswordMismatch()
            return
        }

        if (password.length < 6) {
            view.showPasswordWeak()
            return
        }

        if (model.validateInput(fullName, email, password, confirmPassword)) {
            if (model.isEmailExists(email)) {
                view.showError("Email already registered. Please use a different email.")
                return
            }

            model.saveUser(fullName, email, password)
            view.showSuccess()
            view.navigateToLogin()
            view.clearPasswordFields()
        } else {
            view.showError("Please fill all fields correctly")
        }
    }

    override fun onLoginLinkClick() {
        view.navigateToLogin()
    }
}