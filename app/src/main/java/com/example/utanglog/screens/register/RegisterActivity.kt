package com.example.utanglog.screens.register

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.utanglog.R
import com.example.utanglog.screens.login.LoginActivity

class RegisterActivity : Activity(), RegisterContract.View {

    private lateinit var presenter: RegisterPresenter
    private lateinit var edittextFullName: EditText
    private lateinit var edittextEmail: EditText
    private lateinit var edittextPassword: EditText
    private lateinit var edittextConfirmPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        presenter = RegisterPresenter(this, RegisterModel(this))

        edittextFullName = findViewById(R.id.edittextFullName)
        edittextEmail = findViewById(R.id.edittextEmail)
        edittextPassword = findViewById(R.id.edittextPassword)
        edittextConfirmPassword = findViewById(R.id.edittextConfirmPassword)

        findViewById<Button>(R.id.buttonRegister).setOnClickListener {
            presenter.onRegisterClick()
        }

        findViewById<TextView>(R.id.textviewLoginLink).setOnClickListener {
            presenter.onLoginLinkClick()
        }
    }

    override fun getFullName(): String = edittextFullName.text.toString()
    override fun getEmail(): String = edittextEmail.text.toString()
    override fun getPassword(): String = edittextPassword.text.toString()
    override fun getConfirmPassword(): String = edittextConfirmPassword.text.toString()

    override fun showSuccess() {
        Toast.makeText(this, "Registration Successful! Please Login", Toast.LENGTH_LONG).show()
    }

    override fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun showPasswordMismatch() {
        Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show()
    }

    override fun showPasswordWeak() {
        Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
    }

    override fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun clearPasswordFields() {
        edittextPassword.text.clear()
        edittextConfirmPassword.text.clear()
    }
}