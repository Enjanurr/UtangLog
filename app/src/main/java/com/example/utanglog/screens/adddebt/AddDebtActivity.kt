package com.example.utanglog.screens.addDebt

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import com.example.utanglog.R
import com.example.utanglog.screens.displayDebt.DisplayDebtActivity
import com.example.utanglog.utils.getEditTextValue
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

        findViewById<ImageView>(R.id.back_arrow).setOnClickListener {
            finish()
        }

        findViewById<Button>(R.id.btn_submit).setOnClickListener {
            presenter.onSubmitClick()
        }
    }

    private fun setupStatusSpinner() {
        statusSpinner = findViewById(R.id.spinner_status)
        val statusOptions = listOf("Pending", "Overdue", "Paid")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, statusOptions)
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

        findViewById<EditText>(R.id.et_due_date).setOnClickListener {
            datePickerDialog.show()
        }
    }

    override fun getName(): String = getEditTextValue(R.id.et_name)

    override fun getAmount(): Double {
        val amountStr = getEditTextValue(R.id.et_amount)
        return amountStr.toDoubleOrNull() ?: 0.0
    }

    override fun getStatus(): String = statusSpinner.selectedItem.toString()

    override fun getDueDate(): String = getEditTextValue(R.id.et_due_date)

    override fun getAddress(): String = getEditTextValue(R.id.et_address)

    override fun showSuccess(name: String, amount: Double) {
        Toast.makeText(this, "Added: $name - ₱$amount", Toast.LENGTH_SHORT).show()
    }

    override fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun closeScreen(name: String, amount: Double, status: String, dueDate: String, address: String) {
        // Navigate to DisplayDebtActivity instead of just finishing
        val intent = Intent(this, DisplayDebtActivity::class.java).apply {
            putExtra("name", name)
            putExtra("amount", amount)
            putExtra("status", status)
            putExtra("dueDate", dueDate)
            putExtra("address", address)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) // Clear back stack
        }
        startActivity(intent)
        finish()
    }
}