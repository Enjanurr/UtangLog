package com.example.utanglog.screens.debtdetail

import com.example.utanglog.data.People

class DebtDetailPresenter(
    private val view: DebtDetailContract.View,
    private val model: DebtDetailModel
) : DebtDetailContract.Presenter {

    override fun loadDebtDetails(people: People) {
        view.showDebtDetails(people)
    }

    override fun onDeleteClick(people: People, position: Int) {
        view.showDeleteConfirmation()
    }

    override fun onEditClick(people: People) {
        view.showEditScreen(people)
    }

    override fun onUpdateClick(updatedPeople: People, position: Int) {
        view.onUpdateSuccess(updatedPeople, position)
        view.showMessage("Debt updated successfully!")
    }
}