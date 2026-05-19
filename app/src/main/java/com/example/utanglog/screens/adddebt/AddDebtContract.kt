package com.example.utanglog.screens.adddebt

interface AddDebtContract {
    interface View {
        fun getName(): String
        fun getAmount(): Double
        fun getStatus(): String
        fun getDueDate(): String
        fun getAddress(): String
        fun getPhotoRes(): String  // Changed to String for path
        fun showSuccess(name: String, amount: Double)
        fun showError(message: String)
        fun closeScreen(name: String, amount: Double, status: String, dueDate: String, address: String, photoPath: String)
    }

    interface Presenter {
        fun onSubmitClick()
    }
}