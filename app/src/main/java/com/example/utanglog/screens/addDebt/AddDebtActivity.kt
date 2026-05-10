package com.example.utanglog.screens.addDebt

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import com.example.utanglog.R
import com.example.utanglog.data.StatusOption
import com.example.utanglog.utils.*
import java.util.Calendar

class AddDebtActivity : Activity(), AddDebtContract.View {

    private lateinit var presenter: AddDebtPresenter
    private lateinit var datePickerDialog: DatePickerDialog
    private lateinit var statusSpinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_debt)

        presenter = AddDebtPresenter(this, AddDebtModel())

        setupStatusSpinner()
        setupDatePicker()

        val submitButton = getButton(R.id.btn_add_record)
        submitButton.setOnClickListener {
            presenter.onSubmitClick()
        }

        findViewById<ImageView>(R.id.back_arrow).setOnClickListener {
            finish()
        }
    }

    private fun setupStatusSpinner() {
        statusSpinner = findViewById(R.id.spinner_status)

        val statusOptions = listOf(
            StatusOption("PENDING", "Pending"),
            StatusOption("OVERDUE", "Overdue"),
            StatusOption("PAID", "Paid")
        )

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            statusOptions.map { it.displayName }
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        statusSpinner.adapter = adapter
    }

    private fun setupDatePicker() {
        val today = Calendar.getInstance()
        datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val date = "$dayOfMonth/${month + 1}/$year"
                findViewById<EditText>(R.id.et_due_date).setText(date)
            },
            today.get(Calendar.YEAR),
            today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH)
        )

        val dateInput = findViewById<EditText>(R.id.et_due_date)
        dateInput.setOnClickListener {
            datePickerDialog.show()
        }
    }

    override fun getName(): String = getEditTextValue(R.id.et_name)

    override fun getAmount(): Double {
        val amountStr = getEditTextValue(R.id.et_amount)
        return amountStr.toDoubleOrNull() ?: 0.0
    }

    override fun getStatus(): String {
        val selectedPosition = statusSpinner.selectedItemPosition
        return when (selectedPosition) {
            0 -> "PENDING"
            1 -> "OVERDUE"
            2 -> "PAID"
            else -> "PENDING"
        }
    }

    override fun getDueDate(): String = getEditTextValue(R.id.et_due_date)

    override fun getAddress(): String = getEditTextValue(R.id.et_address)

    override fun showSuccess(name: String, amount: Double) {
        showToast("Debt added: $name - ₱$amount")
    }

    override fun showError(message: String) {
        showToast(message)
    }

    override fun closeScreen(name: String, amount: Double, status: String, dueDate: String, address: String) {
        val resultIntent = Intent().apply {
            putExtra("name", name)
            putExtra("amount", amount)
            putExtra("status", status)
            putExtra("dueDate", dueDate)
            putExtra("address", address)
        }
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }
}