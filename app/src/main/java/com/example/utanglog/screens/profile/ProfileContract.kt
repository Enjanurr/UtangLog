package com.example.utanglog.screens.profile

interface ProfileContract {
    interface View {
        fun showUserProfile(name: String, email: String, phone: String, profileImagePath: String)
        fun showEditDialog(name: String, email: String, phone: String)
        fun showImagePickerDialog()
        fun showUpdateSuccess()
        fun showError(message: String)
        fun closeScreen()
        fun updateProfileImage(imagePath: String)
        fun showLogoutConfirmation()  // ADD THIS
        fun navigateToLogin()          // ADD THIS
    }

    interface Presenter {
        fun loadUserProfile()
        fun onEditClick()
        fun onUpdateClick(name: String, email: String, phone: String)
        fun onChangeImageClick()
        fun onImageSelected(imagePath: String)
        fun getLoggedInEmail(): String
        fun onLogoutClick()            // ADD THIS
        fun logout()                   // ADD THIS
    }
}