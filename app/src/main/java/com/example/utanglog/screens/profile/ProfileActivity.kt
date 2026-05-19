package com.example.utanglog.screens.profile

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.utanglog.R
import com.example.utanglog.screens.adddebt.AddDebtActivity
import com.example.utanglog.screens.displaydebt.DisplayDebtActivity
import com.example.utanglog.screens.login.LoginActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.File
import java.io.FileOutputStream
import kotlin.math.min

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
        setupClickListeners()
        presenter.loadUserProfile()
    }

    private fun setupClickListeners() {
        findViewById<Button>(R.id.buttonEditProfile).setOnClickListener {
            presenter.onEditClick()
        }

        // ADD THIS - Logout button
        findViewById<Button>(R.id.buttonLogout).setOnClickListener {
            presenter.onLogoutClick()
        }

        imageviewAvatar.setOnClickListener {
            openGallery()
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
            if (!file.exists()) file.mkdirs()

            val imageFile = File(file, "profile_${System.currentTimeMillis()}.jpg")
            val outputStream = FileOutputStream(imageFile)

            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()

            imageFile.absolutePath
        } catch (e: Exception) {
            null
        }
    }

    private fun loadCircularImage(imagePath: String) {
        try {
            val bitmap = BitmapFactory.decodeFile(imagePath)
            if (bitmap != null) {
                imageviewAvatar.setImageBitmap(makeCircular(bitmap))
            } else {
                imageviewAvatar.setImageResource(R.drawable.ic_person_placeholder)
            }
        } catch (e: Exception) {
            imageviewAvatar.setImageResource(R.drawable.ic_person_placeholder)
        }
    }

    private fun makeCircular(bitmap: Bitmap): Bitmap {
        val size = min(bitmap.width, bitmap.height)
        val output = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        val paint = Paint().apply { isAntiAlias = true }

        val rect = Rect(0, 0, size, size)
        val srcRect = Rect(
            (bitmap.width - size) / 2,
            (bitmap.height - size) / 2,
            (bitmap.width + size) / 2,
            (bitmap.height + size) / 2
        )

        canvas.drawCircle(size / 2f, size / 2f, size / 2f, paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, srcRect, rect, paint)

        return output
    }

    override fun showUserProfile(fullName: String, email: String, phone: String, profileImagePath: String) {
        textviewName.text = fullName
        textviewEmail.text = email
        textviewPhone.text = phone

        if (profileImagePath.isNotEmpty()) {
            loadCircularImage(profileImagePath)
        } else {
            imageviewAvatar.setImageResource(R.drawable.ic_person_placeholder)
        }
    }

    override fun showEditDialog(fullName: String, email: String, phone: String) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_profile, null)

        val edittextName = dialogView.findViewById<EditText>(R.id.edittextName)
        val edittextEmail = dialogView.findViewById<EditText>(R.id.edittextEmail)
        val edittextPhone = dialogView.findViewById<EditText>(R.id.edittextPhone)

        edittextName.setText(fullName)
        edittextEmail.setText(email)
        edittextPhone.setText(phone)

        AlertDialog.Builder(this)
            .setTitle("Edit Profile")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                presenter.onUpdateClick(
                    edittextName.text.toString(),
                    edittextEmail.text.toString(),
                    edittextPhone.text.toString()
                )
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun showImagePickerDialog() {
        openGallery()
    }

    override fun showUpdateSuccess() {
        Toast.makeText(this, "Profile updated!", Toast.LENGTH_SHORT).show()
    }

    override fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun closeScreen() {
        finish()
    }

    override fun updateProfileImage(imagePath: String) {
        loadCircularImage(imagePath)
    }

    override fun showLogoutConfirmation() {
        AlertDialog.Builder(this)
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Yes") { _, _ -> presenter.logout() }
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