package com.example.utanglog.screens.displaydebt

import com.example.utanglog.R
import com.example.utanglog.data.People

class DisplayDebtModel {
    companion object {
        private val debtList = mutableListOf<People>()
        // REMOVE the init block with sample data
    }

    fun getDebts(): List<People> {
        return debtList.sortedWith(compareBy(
            { it.status == "Paid" },
            { debtList.indexOf(it) }
        ))
    }

    fun addDebt(people: People) {
        debtList.add(0, people)
        println("Added debt: ${people.name}, List size: ${debtList.size}")
    }

    fun updateDebt(updatedPeople: People, position: Int) {
        if (position >= 0 && position < debtList.size) {
            debtList[position] = updatedPeople
            println("Updated debt at position $position")
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