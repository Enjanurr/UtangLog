package com.example.utanglog.screens.debtdetail

import com.example.utanglog.data.People

class DebtDetailModel {
    // This needs to be connected to your actual data source
    // For now, create a temporary list or get from DisplayDebtModel

    // Option A: Create a temporary list (not ideal for updates)
    private val debtList = mutableListOf<People>()

    fun getPeopleAtPosition(position: Int): People {
        // This should actually fetch from your global data source
        // For now, return empty People if position is invalid
        return if (position >= 0 && position < debtList.size) {
            debtList[position]
        } else {
            People()
        }
    }
}