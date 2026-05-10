package com.example.utanglog.screens.addDebt

import com.example.utanglog.R

class AddDebtPresenter(
    private val view: AddDebtContract.View,
    private val model: AddDebtModel
) : AddDebtContract.Presenter {

    override fun onSubmitClick() {
        val name = view.getName()
        val amount = view.getAmount()  // Now Double

        if (model.validateInput(name, amount)) {
            view.showSuccess(name, amount)
            view.closeScreen(name, amount)
        } else {
            view.showError("Please fill both name and amount")
        }
    }
}