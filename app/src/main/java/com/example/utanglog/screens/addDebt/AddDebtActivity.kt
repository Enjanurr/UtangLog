package com.example.utanglog.screens.addDebt

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.example.utanglog.R
import com.example.utanglog.utils.*

class AddDebtActivity : Activity(), AddDebtContract.View {  // ← IMPLEMENTS the View interface

    private lateinit var presenter: AddDebtPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_debt)

        presenter = AddDebtPresenter(this, AddDebtModel())  // ← Initialize presenter

        val submitButton = getButton(R.id.addDebtButton)

        submitButton.setOnClickListener {
            presenter.onSubmitClick()  // ← Let presenter handle logic
        }
    }

    override fun getName(): String = getEditTextValue(R.id.edittextAddName)

    override fun getAmount(): Double {
        val amountStr = getEditTextValue(R.id.edittextAmount)
        return amountStr.toDoubleOrNull() ?: 0.0
    }

    override fun showSuccess(name: String, amount: Double) {
        showToast("Debt added: $name - ₱$amount")
    }

    override fun showError(message: String) {
        showToast(message)
    }

    override fun closeScreen(name: String, amount: Double) {
        val resultIntent = Intent().apply {
            putExtra("name", name)
            putExtra("amount", amount)
        }
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }
}