package com.example.utanglog.screens.adddebt

class AddDebtModel {
    fun validateInput(
        name: String,
        amount: Double,
        status: String,
        dueDate: String,
        address: String
    ): Boolean {
        return name.isNotEmpty() &&
                amount > 0.0 &&
                status.isNotEmpty() &&
                dueDate.isNotEmpty() &&
                address.isNotEmpty()
    }
}