package com.example.utanglog.screens.displayDebt

import com.example.utanglog.data.People

class DisplayDebtPresenter(
    private val view: DisplayDebtContract.View,
    private val model: DisplayDebtModel
) : DisplayDebtContract.Presenter {

    override fun loadDebts() {
        val debts = model.getDebts()
        view.showDebtList(debts)
    }

    override fun addDebt(people: People) {
        model.addDebt(people)
        val updatedDebts = model.getDebts()
        view.showDebtList(updatedDebts)
        view.showDebtAdded()
    }

    override fun deleteDebt(position: Int) {
        val deletedPeople = model.deleteDebt(position)
        val updatedDebts = model.getDebts()
        view.showDebtList(updatedDebts)
        view.showDebtDeleted(deletedPeople)
    }

    override fun onItemClick(people: People) {
        view.showItemClicked(people)
    }

    override fun onImageClick(people: People) {
        view.showImageClicked(people)
    }

    override fun onActivityResult(name: String, amount: Double, photoRes: Int) {  // amount as Double
        if (name.isNotEmpty() && amount > 0.0) {  // Check if amount is valid
            val newPeople = People(name, amount, photoRes)
            addDebt(newPeople)
        }
    }

    override fun onDeleteClick(people: People, position: Int) {
        view.showDeleteConfirmation(people, position)
    }
}