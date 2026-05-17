package com.example.utanglog.screens.addDebt

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import com.example.utanglog.R
import com.example.utanglog.screens.displayDebt.DisplayDebtActivity
import com.example.utanglog.screens.profile.ProfileActivity
import com.example.utanglog.utils.getEditTextValue
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.Calendar

class AddDebtActivity : Activity(), AddDebtContract.View {

    private lateinit var presenter: AddDebtPresenter
    private lateinit var datePickerDialog: DatePickerDialog
    private lateinit var statusSpinner: Spinner
    private lateinit var imageviewDebtorAvatar: ImageView
    private var selectedPhotoRes: Int = R.drawable.profile  // Default image
    private val PICK_IMAGE_REQUEST = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_debt)

        presenter = AddDebtPresenter(this, AddDebtModel())

        setupStatusSpinner()
        setupDatePicker()
        setupBottomNavigation()

        imageviewDebtorAvatar = findViewById(R.id.imageviewDebtorAvatar)



        findViewById<Button>(R.id.btn_submit).setOnClickListener {
            presenter.onSubmitClick()
        }

        imageviewDebtorAvatar.setOnClickListener {
            showImagePickerDialog()
        }
    }

    private fun showImagePickerDialog() {
        val options = arrayOf("Choose from Gallery", "Cancel")
        AlertDialog.Builder(this)
            .setTitle("Select Debtor Photo")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> openGallery()
                }
            }
            .show()
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            // For now, just use a different placeholder
            // In real app, you would save the image and set the path
            selectedPhotoRes = R.drawable.profile  // Keep default for now
            imageviewDebtorAvatar.setImageResource(selectedPhotoRes)
            Toast.makeText(this, "Photo selected (using default)", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupBottomNavigation() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNav.selectedItemId = R.id.nav_add
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    finish()
                    true
                }
                R.id.nav_add -> {
                    true
                }
                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }
    }

    private fun setupStatusSpinner() {
        statusSpinner = findViewById(R.id.spinnerStatus)
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
                findViewById<EditText>(R.id.edittextDueDate).setText(date)
            },
            today.get(Calendar.YEAR),
            today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH)
        )

        findViewById<EditText>(R.id.edittextDueDate).setOnClickListener {
            datePickerDialog.show()
        }
    }

    override fun getName(): String = getEditTextValue(R.id.edittextName)

    override fun getAmount(): Double {
        val amountStr = getEditTextValue(R.id.edittextAmount)
        return amountStr.toDoubleOrNull() ?: 0.0
    }

    override fun getStatus(): String = statusSpinner.selectedItem.toString()

    override fun getDueDate(): String = getEditTextValue(R.id.edittextDueDate)

    override fun getAddress(): String = getEditTextValue(R.id.edittextAddress)

    override fun getPhotoRes(): Int = selectedPhotoRes

    override fun showSuccess(name: String, amount: Double) {
        Toast.makeText(this, "Added: $name - ₱$amount", Toast.LENGTH_SHORT).show()
    }

    override fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun closeScreen(name: String, amount: Double, status: String, dueDate: String, address: String, photoRes: Int) {
        val intent = Intent(this, DisplayDebtActivity::class.java).apply {
            putExtra("name", name)
            putExtra("amount", amount)
            putExtra("status", status)
            putExtra("dueDate", dueDate)
            putExtra("address", address)
            putExtra("photoRes", photoRes)
        }
        startActivity(intent)
        finish()
    }
}