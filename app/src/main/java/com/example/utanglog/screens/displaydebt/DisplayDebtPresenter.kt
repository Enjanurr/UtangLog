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

    override fun addDebtFromIntent(name: String, amount: Double, status: String, dueDate: String, address: String) {
        if (name.isNotEmpty() && amount > 0.0) {
            val newDebt = People(name, amount, dueDate, status, address)
            addDebt(newDebt)
        }
    }

    override fun onItemClick(people: People) {
        view.showItemClicked(people)
    }
}