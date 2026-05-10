package com.example.utanglog.screens.addDebt

interface AddDebtContract {
    interface View {
        fun getName(): String
        fun getAmount(): Double
        fun getStatus(): String
        fun getDueDate(): String
        fun getAddress(): String
        fun showSuccess(name: String, amount: Double)
        fun showError(message: String)
        fun closeScreen(name: String, amount: Double, status: String, dueDate: String, address: String)
    }

    interface Presenter {
        fun onSubmitClick()
    }
}