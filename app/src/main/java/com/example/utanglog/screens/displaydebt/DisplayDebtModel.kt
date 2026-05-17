package com.example.utanglog.screens.displayDebt

import com.example.utanglog.R
import com.example.utanglog.data.People

class DisplayDebtModel {
    companion object {
        private val debtList = mutableListOf<People>()

        init {
            if (debtList.isEmpty()) {
                debtList.addAll(listOf(
                    People("John Doe", 2500.0, "2024-12-31", "Pending", "123 Main St", R.drawable.profile),
                    People("Jane Smith", 980.0, "2024-11-15", "Overdue", "456 Oak Ave", R.drawable.profile),
                    People("Mike Brown", 1750.0, "2025-01-10", "Paid", "789 Pine Rd", R.drawable.profile)
                ))
            }
        }
    }

    fun getDebts(): List<People> {
        return debtList.sortedWith(compareBy(
            { it.status == "Paid" },  // Paid = true goes to bottom
            { debtList.indexOf(it) }   // Keep original order within groups
        ))
    }

    fun addDebt(people: People) {
        debtList.add(0, people) // Add to top (index 0) for latest first
    }

    fun updateDebt(updatedPeople: People, position: Int) {
        if (position >= 0 && position < debtList.size) {
            debtList[position] = updatedPeople
        }
    }

    fun deleteDebt(position: Int): People? {
        return if (position >= 0 && position < debtList.size) {
            debtList.removeAt(position)
        } else {
            null
        }
    }

    fun getTotalAmount(): Double = debtList.sumOf { it.amount }

    fun getDebtCount(): Int = debtList.size
}