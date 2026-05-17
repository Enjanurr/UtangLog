package com.example.utanglog.screens.profile

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.utanglog.R
import com.example.utanglog.screens.addDebt.AddDebtActivity
import com.example.utanglog.screens.displayDebt.DisplayDebtActivity
import com.example.utanglog.screens.login.LoginActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.File
import java.io.FileOutputStream

class ProfileActivity : Activity(), ProfileContract.View {

    private lateinit var presenter: ProfilePresenter
    private lateinit var textviewName: TextView
    private lateinit var textviewEmail: TextView
    private lateinit var textviewPhone: TextView
    private lateinit var imageviewAvatar: ImageView

    private val PICK_IMAGE_REQUEST = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        presenter = ProfilePresenter(this, ProfileModel(this))

        textviewName = findViewById(R.id.textviewName)
        textviewEmail = findViewById(R.id.textviewEmail)
        textviewPhone = findViewById(R.id.textviewPhone)
        imageviewAvatar = findViewById(R.id.imageviewAvatar)

        setupBottomNavigation()
        setupViews()
        presenter.loadUserProfile()
    }

    private fun setupViews() {
        findViewById<Button>(R.id.buttonEditProfile).setOnClickListener {
            presenter.onEditClick()
        }

        findViewById<Button>(R.id.buttonLogout).setOnClickListener {
            presenter.onLogoutClick()
        }

        imageviewAvatar.setOnClickListener {
            presenter.onChangeImageClick()
        }
    }

    private fun setupBottomNavigation() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNav.selectedItemId = R.id.nav_profile
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, DisplayDebtActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_add -> {
                    startActivity(Intent(this, AddDebtActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_profile -> {
                    true
                }
                else -> false
            }
        }
    }

    override fun showImagePickerDialog() {
        val options = arrayOf("Choose from Gallery", "Cancel")
        AlertDialog.Builder(this)
            .setTitle("Select Profile Picture")
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
            val imageUri = data?.data
            if (imageUri != null) {
                val imagePath = saveImageToStorage(imageUri)
                if (imagePath != null) {
                    presenter.onImageSelected(imagePath)
                }
            }
        }
    }

    private fun saveImageToStorage(imageUri: Uri): String? {
        return try {
            val inputStream = contentResolver.openInputStream(imageUri)
            val file = File(filesDir, "profile_images")
            if (!file.exists()) {
                file.mkdirs()
            }

            val fileName = "profile_${System.currentTimeMillis()}.jpg"
            val imageFile = File(file, fileName)
            val outputStream = FileOutputStream(imageFile)

            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()

            imageFile.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun loadImageIntoView(imagePath: String) {
        try {
            val bitmap = BitmapFactory.decodeFile(imagePath)
            if (bitmap != null) {
                imageviewAvatar.setImageBitmap(bitmap)
            } else {
                imageviewAvatar.setImageResource(R.drawable.ic_person_placeholder)
            }
        } catch (e: Exception) {
            imageviewAvatar.setImageResource(R.drawable.ic_person_placeholder)
        }
    }

    override fun showUserProfile(name: String, email: String, phone: String, profileImagePath: String) {
        textviewName.text = name
        textviewEmail.text = email
        textviewPhone.text = phone

        if (profileImagePath.isNotEmpty()) {
            loadImageIntoView(profileImagePath)
        } else {
            imageviewAvatar.setImageResource(R.drawable.ic_person_placeholder)
        }
    }

    override fun showEditDialog(name: String, email: String, phone: String) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_profile, null)

        val edittextName = dialogView.findViewById<EditText>(R.id.edittextName)
        val edittextEmail = dialogView.findViewById<EditText>(R.id.edittextEmail)
        val edittextPhone = dialogView.findViewById<EditText>(R.id.edittextPhone)

        edittextName.setText(name)
        edittextEmail.setText(email)
        edittextPhone.setText(phone)

        AlertDialog.Builder(this)
            .setTitle("Edit Profile")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val updatedName = edittextName.text.toString()
                val updatedEmail = edittextEmail.text.toString()
                val updatedPhone = edittextPhone.text.toString()
                presenter.onUpdateClick(updatedName, updatedEmail, updatedPhone)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun showUpdateSuccess() {
        Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
    }

    override fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun closeScreen() {
        finish()
    }

    override fun updateProfileImage(imagePath: String) {
        loadImageIntoView(imagePath)
    }

    // ADD LOGOUT METHODS
    override fun showLogoutConfirmation() {
        AlertDialog.Builder(this)
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Yes") { _, _ ->
                presenter.logout()
            }
            .setNegativeButton("No", null)
            .show()
    }

    override fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}