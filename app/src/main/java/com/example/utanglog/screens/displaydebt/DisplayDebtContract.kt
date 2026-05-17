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
        fun showMessage(message: String)
    }

    interface Presenter {
        fun loadDebts()
        fun addDebt(people: People)
        fun updateDebt(updatedPeople: People, position: Int)
        fun deleteDebt(position: Int)
        fun onItemClick(people: People)
        fun onActivityResult(name: String, amount: Double, status: String, dueDate: String, address: String, photoRes: Int)
    }
}