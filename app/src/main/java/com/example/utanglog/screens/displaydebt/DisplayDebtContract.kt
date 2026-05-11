package com.example.utanglog.screens.displayDebt

import com.example.utanglog.data.People

interface DisplayDebtContract {
    interface View {
        fun showDebtList(debts: List<People>)
        fun showDebtAdded()
        fun showItemClicked(people: People)
        fun refreshListView()
        fun navigateToAddDebt()
        fun updateSummary(total: Double, count: Int)
    }

    interface Presenter {
        fun loadDebts()
        fun addDebt(people: People)
        fun addDebtFromIntent(name: String, amount: Double, status: String, dueDate: String, address: String)  // ADD THIS
        fun onItemClick(people: People)
    }
}