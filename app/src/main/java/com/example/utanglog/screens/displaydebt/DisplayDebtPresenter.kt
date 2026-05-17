package com.example.utanglog.screens.displayDebt

import com.example.utanglog.data.People

class DisplayDebtPresenter(
    private val view: DisplayDebtContract.View,
    private val model: DisplayDebtModel
) : DisplayDebtContract.Presenter {

    override fun loadDebts() {
        val debts = model.getDebts()
        view.showDebtList(debts)
        view.updateSummary(model.getTotalAmount(), model.getDebtCount())
    }

    override fun addDebt(people: People) {
        model.addDebt(people)
        view.showDebtList(model.getDebts())
        view.updateSummary(model.getTotalAmount(), model.getDebtCount())
        view.showDebtAdded()
    }

    override fun updateDebt(updatedPeople: People, position: Int) {
        model.updateDebt(updatedPeople, position)
        view.showDebtList(model.getDebts())
        view.updateSummary(model.getTotalAmount(), model.getDebtCount())
        view.showMessage("Debt updated successfully!")
    }

    override fun deleteDebt(position: Int) {
        val deletedPeople = model.deleteDebt(position)
        if (deletedPeople != null) {
            view.showDebtList(model.getDebts())
            view.updateSummary(model.getTotalAmount(), model.getDebtCount())
            view.showMessage("Deleted ${deletedPeople.name}'s debt")
        }
    }

    override fun onItemClick(people: People) {
        view.showItemClicked(people)
    }

    override fun onActivityResult(name: String, amount: Double, status: String, dueDate: String, address: String, photoRes: Int) {
        if (name.isNotEmpty() && amount > 0.0) {
            val newDebt = People(
                name = name,
                amount = amount,
                dueDate = dueDate,
                status = status,
                address = address,
                photoRes = photoRes
            )
            addDebt(newDebt)
        }
    }
}