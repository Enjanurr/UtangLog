package com.example.utanglog.screens.debtdetail

import com.example.utanglog.data.People

interface DebtDetailContract {
    interface View {
        fun showDebtDetails(people: People)
        fun showDeleteConfirmation()
        fun showEditScreen(people: People)
        fun closeScreen()
        fun showMessage(message: String)
    }

    interface Presenter {
        fun loadDebtDetails(people: People)
        fun onDeleteClick(people: People, position: Int)
        fun onEditClick(people: People)
    }
}