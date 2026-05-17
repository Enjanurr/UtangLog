package com.example.utanglog.screens.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.utanglog.R
import com.example.utanglog.screens.displayDebt.DisplayDebtActivity
import com.example.utanglog.screens.register.RegisterActivity

class LoginActivity : Activity(), LoginContract.View {

    private lateinit var presenter: LoginPresenter
    private lateinit var edittextEmail: EditText
    private lateinit var edittextPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        presenter = LoginPresenter(this, LoginModel(this))

        edittextEmail = findViewById(R.id.edittextEmail)
        edittextPassword = findViewById(R.id.edittextPassword)

        findViewById<Button>(R.id.buttonLogin).setOnClickListener {
            presenter.onLoginClick()
        }

        findViewById<TextView>(R.id.textviewRegisterLink).setOnClickListener {
            presenter.onRegisterClick()
        }
    }

    override fun getEmail(): String = edittextEmail.text.toString().trim()

    override fun getPassword(): String = edittextPassword.text.toString()

    override fun showSuccess() {
        Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()
    }

    override fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun showEmptyFieldsError() {
        Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
    }

    override fun navigateToHome() {
        val intent = Intent(this, DisplayDebtActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    override fun navigateToRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    override fun clearPasswordField() {
        edittextPassword.text.clear()
    }
}