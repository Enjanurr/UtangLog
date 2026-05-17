package com.example.utanglog.screens.login

class LoginPresenter(
    private val view: LoginContract.View,
    private val model: LoginModel
) : LoginContract.Presenter {

    override fun onLoginClick() {
        val email = view.getEmail()
        val password = view.getPassword()

        if (email.isEmpty() || password.isEmpty()) {
            view.showEmptyFieldsError()
            return
        }

        if (model.validateCredentials(email, password)) {
            model.saveLoginSession(email)
            view.showSuccess()
            view.navigateToHome()
            view.clearPasswordField()
        } else {
            view.showError("Invalid email or password")
        }
    }

    override fun onRegisterClick() {
        view.navigateToRegister()
    }
}