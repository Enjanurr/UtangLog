package com.example.utanglog.screens.displayDebt

import com.example.utanglog.R
import com.example.utanglog.data.People

class DisplayDebtModel {
    private val debtList = mutableListOf(
        People("Vaughn", 1000.0, R.drawable.android),      // Now Double
        People("Dassy", 124123.0, R.drawable.android),     // Now Double
        People("Marco", 12312.0, R.drawable.android),      // Now Double
        People("Angela", 1233123.0, R.drawable.android)    // Now Double
    )

    fun getDebts(): List<People> = debtList.toList()

    fun addDebt(people: People) {
        debtList.add(people)
    }

    fun deleteDebt(position: Int): People {
        return debtList.removeAt(position)
    }

    fun getDebtCount(): Int = debtList.size
}