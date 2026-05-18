package com.example.utanglog.screens.profile

interface ProfileContract {
    interface View {
        fun showUserProfile(fullName: String, email: String, phone: String, profileImagePath: String)  // Changed: name → fullName
        fun showEditDialog(fullName: String, email: String, phone: String)  // Changed: name → fullName
        fun showImagePickerDialog()
        fun showUpdateSuccess()
        fun showError(message: String)
        fun closeScreen()
        fun updateProfileImage(imagePath: String)
        fun showLogoutConfirmation()
        fun navigateToLogin()
    }

    interface Presenter {
        fun loadUserProfile()
        fun onEditClick()
        fun onUpdateClick(name: String, email: String, phone: String)
        fun onChangeImageClick()
        fun onImageSelected(imagePath: String)
        fun getLoggedInEmail(): String
        fun onLogoutClick()
        fun logout()
    }
}