package com.example.utanglog.screens.displayDebt

import com.example.utanglog.R
import com.example.utanglog.data.People

class DisplayDebtModel {
    companion object {
        // Make it a companion object so it persists across Activity instances
        private val debtList = mutableListOf<People>()

        init {
            // Sample data - only added once ever
            if (debtList.isEmpty()) {
                debtList.addAll(listOf(
                    People("John Doe", 2500.0, "2024-12-31", "Pending", "123 Main St"),
                    People("Jane Smith", 980.0, "2024-11-15", "Overdue", "456 Oak Ave"),
                    People("Mike Brown", 1750.0, "2025-01-10", "Paid", "789 Pine Rd")
                ))
            }
        }
    }

    fun getDebts(): List<People> = debtList.toList()

    fun addDebt(people: People) {
        debtList.add(people)  // This ADDS, not replaces
    }

    fun getTotalAmount(): Double = debtList.sumOf { it.amount }

    fun getDebtCount(): Int = debtList.size
}