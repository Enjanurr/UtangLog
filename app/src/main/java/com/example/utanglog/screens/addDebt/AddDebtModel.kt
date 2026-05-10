package com.example.utanglog.screens.addDebt

class AddDebtModel {
    fun validateInput(name: String, amount: Double): Boolean {
        return name.isNotEmpty() && amount > 0.0  // Check if amount > 0
    }
}