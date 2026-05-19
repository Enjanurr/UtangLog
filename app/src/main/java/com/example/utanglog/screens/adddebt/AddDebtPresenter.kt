package com.example.utanglog.screens.adddebt

class AddDebtPresenter(
    private val view: AddDebtContract.View,
    private val model: AddDebtModel
) : AddDebtContract.Presenter {

    override fun onSubmitClick() {
        val name = view.getName()
        val amount = view.getAmount()
        val status = view.getStatus()
        val dueDate = view.getDueDate()
        val address = view.getAddress()
        val photoPath = view.getPhotoRes()

        if (model.validateInput(name, amount, status, dueDate, address)) {
            view.showSuccess(name, amount)
            view.closeScreen(name, amount, status, dueDate, address, photoPath)
        } else {
            view.showError("Please fill all fields correctly")
        }
    }
}