package com.example.utanglog.screens.debtdetail

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.utanglog.R
import com.example.utanglog.screens.addDebt.AddDebtActivity
import com.example.utanglog.screens.displayDebt.DisplayDebtActivity  // ADD THIS IMPORT
import com.example.utanglog.screens.profile.ProfileActivity
import com.example.utanglog.data.People
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.Calendar

class DebtDetailActivity : Activity(), DebtDetailContract.View {

    private lateinit var presenter: DebtDetailPresenter
    private lateinit var currentPeople: People
    private var currentPosition: Int = -1
    private lateinit var datePickerDialog: DatePickerDialog
    private lateinit var imageviewAvatar: ImageView
    private var selectedPhotoRes: Int = R.drawable.profile
    private val PICK_IMAGE_REQUEST = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_debt_detail)

        presenter = DebtDetailPresenter(this, DebtDetailModel())

        currentPeople = intent.getSerializableExtra("people") as People
        currentPosition = intent.getIntExtra("position", -1)
        selectedPhotoRes = currentPeople.photoRes

        setupViews()
        setupBottomNavigation()
        presenter.loadDebtDetails(currentPeople)
    }

    private fun setupViews() {
        imageviewAvatar = findViewById(R.id.imageviewAvatar)



        findViewById<Button>(R.id.buttonEdit).setOnClickListener {
            showEditDialog(currentPeople, currentPosition)
        }

        findViewById<Button>(R.id.buttonDelete).setOnClickListener {
            presenter.onDeleteClick(currentPeople, currentPosition)
        }

        imageviewAvatar.setOnClickListener {
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
            selectedPhotoRes = R.drawable.profile
            imageviewAvatar.setImageResource(selectedPhotoRes)
            Toast.makeText(this, "Photo selected (will update on save)", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupBottomNavigation() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNav.selectedItemId = R.id.nav_home
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, DisplayDebtActivity::class.java))  // FIXED
                    finish()
                    true
                }
                R.id.nav_add -> {
                    startActivity(Intent(this, AddDebtActivity::class.java))
                    finish()
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

    private fun showEditDialog(people: People, position: Int) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_debt, null)

        val edittextName = dialogView.findViewById<EditText>(R.id.edittextName)
        val edittextAmount = dialogView.findViewById<EditText>(R.id.edittextAmount)
        val spinnerStatus = dialogView.findViewById<Spinner>(R.id.spinnerStatus)
        val edittextDueDate = dialogView.findViewById<EditText>(R.id.edittextDueDate)
        val edittextAddress = dialogView.findViewById<EditText>(R.id.edittextAddress)

        edittextName.setText(people.name)
        edittextAmount.setText(people.amount.toString())
        edittextAddress.setText(people.address)

        val statusOptions = listOf("Pending", "Overdue", "Paid")
        val statusAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, statusOptions)
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerStatus.adapter = statusAdapter

        val statusPosition = statusOptions.indexOf(people.status)
        if (statusPosition >= 0) {
            spinnerStatus.setSelection(statusPosition)
        }

        edittextDueDate.setText(people.dueDate)
        edittextDueDate.isFocusable = false
        edittextDueDate.isClickable = true
        edittextDueDate.setOnClickListener {
            showDatePickerDialog(edittextDueDate)
        }

        AlertDialog.Builder(this)
            .setTitle("Edit Debt")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val updatedPeople = People(
                    name = edittextName.text.toString(),
                    amount = edittextAmount.text.toString().toDoubleOrNull() ?: 0.0,
                    dueDate = edittextDueDate.text.toString(),
                    status = spinnerStatus.selectedItem.toString(),
                    address = edittextAddress.text.toString(),
                    photoRes = selectedPhotoRes
                )
                presenter.onUpdateClick(updatedPeople, position)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showDatePickerDialog(edittextDueDate: EditText) {
        val calendar = Calendar.getInstance()
        datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val date = "$dayOfMonth/${month + 1}/$year"
                edittextDueDate.setText(date)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    override fun showDebtDetails(people: People) {
        findViewById<TextView>(R.id.textviewName).text = people.name
        findViewById<TextView>(R.id.textviewAmount).text = String.format("₱%.2f", people.amount)
        findViewById<TextView>(R.id.textviewDueDate).text = people.dueDate
        findViewById<TextView>(R.id.textviewAddress).text = people.address
        imageviewAvatar.setImageResource(people.photoRes)

        val textviewStatus = findViewById<TextView>(R.id.textviewStatus)
        textviewStatus.text = people.status

        when (people.status) {
            "Paid" -> {
                textviewStatus.setBackgroundColor(ContextCompat.getColor(this, R.color.status_paid_bg))
                textviewStatus.setTextColor(ContextCompat.getColor(this, R.color.status_paid_text))
            }
            "Overdue" -> {
                textviewStatus.setBackgroundColor(ContextCompat.getColor(this, R.color.status_overdue_bg))
                textviewStatus.setTextColor(ContextCompat.getColor(this, R.color.status_overdue_text))
            }
            else -> {
                textviewStatus.setBackgroundColor(ContextCompat.getColor(this, R.color.status_pending_bg))
                textviewStatus.setTextColor(ContextCompat.getColor(this, R.color.status_pending_text))
            }
        }
    }

    override fun showDeleteConfirmation() {
        AlertDialog.Builder(this)
            .setTitle("Delete Debt")
            .setMessage("Are you sure you want to delete ${currentPeople.name}'s debt?")
            .setPositiveButton("Delete") { _, _ ->
                val resultIntent = Intent().apply {
                    putExtra("delete_position", currentPosition)
                }
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun showEditScreen(people: People) = Unit

    override fun onUpdateSuccess(updatedPeople: People, position: Int) {
        val resultIntent = Intent().apply {
            putExtra("update_position", position)
            putExtra("updated_name", updatedPeople.name)
            putExtra("updated_amount", updatedPeople.amount)
            putExtra("updated_status", updatedPeople.status)
            putExtra("updated_dueDate", updatedPeople.dueDate)
            putExtra("updated_address", updatedPeople.address)
            putExtra("updated_photoRes", updatedPeople.photoRes)
        }
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

    override fun closeScreen() {
        finish()
    }

    override fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}