package com.example.utanglog.screens.profile

class ProfilePresenter(
    private val view: ProfileContract.View,
    private val model: ProfileModel
) : ProfileContract.Presenter {

    override fun loadUserProfile() {
        val user = model.getUser()
        view.showUserProfile(user.fullName, user.email, user.phone, user.profileImagePath)  // Changed: name → fullName
    }

    override fun onEditClick() {
        val user = model.getUser()
        view.showEditDialog(user.fullName, user.email, user.phone)  // Changed: name → fullName
    }

    override fun onUpdateClick(name: String, email: String, phone: String) {
        if (model.updateUser(name, email, phone)) {
            view.showUpdateSuccess()
            val user = model.getUser()
            view.showUserProfile(user.fullName, user.email, user.phone, user.profileImagePath)  // Changed: name → fullName
        } else {
            view.showError("Name and Email are required")
        }
    }

    override fun onChangeImageClick() {
        view.showImagePickerDialog()
    }

    override fun onImageSelected(imagePath: String) {
        model.saveProfileImagePath(imagePath)
        view.updateProfileImage(imagePath)
        view.showUpdateSuccess()
    }

    override fun getLoggedInEmail(): String {
        return model.getLoggedInEmail()
    }

    override fun onLogoutClick() {
        view.showLogoutConfirmation()
    }

    override fun logout() {
        if (model.logout()) {
            view.navigateToLogin()
        } else {
            view.showError("Logout failed")
        }
    }
}