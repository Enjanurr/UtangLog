package com.example.utanglog.screens.addDebt

interface AddDebtContract {
    interface View {
        fun getName(): String
        fun getAmount(): Double  // Changed to Double
        fun showSuccess(name: String, amount: Double)  // Changed to Double
        fun showError(message: String)
        fun closeScreen(name: String, amount: Double)  // Changed to Double
    }

    interface Presenter {
        fun onSubmitClick()
    }
}