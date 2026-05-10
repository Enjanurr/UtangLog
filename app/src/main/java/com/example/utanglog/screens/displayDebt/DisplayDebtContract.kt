package com.example.utanglog.screens.displayDebt

import com.example.utanglog.data.People

interface DisplayDebtContract {
    interface View {
        fun showDebtList(debts: List<People>)
        fun showDeleteConfirmation(people: People, position: Int)
        fun showDebtAdded()
        fun showDebtDeleted(people: People)
        fun showItemClicked(people: People)
        fun showImageClicked(people: People)
        fun refreshListView()
        fun navigateToAddDebt()
    }

    interface Presenter {
        fun loadDebts()
        fun addDebt(people: People)
        fun deleteDebt(position: Int)
        fun onItemClick(people: People)
        fun onImageClick(people: People)
        fun onActivityResult(name: String, amount: Double, photoRes: Int)  // amount as Double
        fun onDeleteClick(people: People, position: Int)
    }
}