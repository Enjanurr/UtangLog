package com.example.utanglog.screens.login

class LoginPresenter(
    private val view: LoginContract.View,
    private val model: LoginModel
) : LoginContract.Presenter {

    override fun onLoginClick() {
        val email = view.getEmail()
        val password = view.getPassword()

        // Check empty fields
        if (email.isEmpty() || password.isEmpty()) {
            view.showEmptyFieldsError()
            return
        }

        // Check if account exists
        if (!model.doesAccountExist(email)) {
            view.showAccountNotFound()
            return
        }

        // Validate credentials
        if (model.validateCredentials(email, password)) {
            model.saveLoginSession(email)
            view.showSuccess()
            view.navigateToHome()
            view.clearPasswordField()
        } else {
            view.showInvalidCredentials()
        }
    }

    override fun onRegisterClick() {
        view.navigateToRegister()
    }
}