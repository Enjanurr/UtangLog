package com.example.utanglog.screens.displayDebt

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import com.example.utanglog.R
import com.example.utanglog.screens.addDebt.AddDebtActivity
import com.example.utanglog.data.People
import com.example.utanglog.helper.CustomListViewAdapter

class DisplayDebtActivity : Activity(), DisplayDebtContract.View {

    private lateinit var presenter: DisplayDebtPresenter
    private lateinit var adapter: CustomListViewAdapter
    private lateinit var listView: ListView
    private val ADD_DEBT_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_debt)

        presenter = DisplayDebtPresenter(this, DisplayDebtModel())

        listView = findViewById<ListView>(R.id.listView)
        val addNewDebtButton = findViewById<Button>(R.id.addNewDebtButton)

        adapter = CustomListViewAdapter(
            this,
            mutableListOf(),
            onImageClick = { people -> presenter.onImageClick(people) },
            onItemClick = { people -> presenter.onItemClick(people) },
            onDeleteClick = { people, position -> presenter.onDeleteClick(people, position) }
        )
        listView.adapter = adapter

        addNewDebtButton.setOnClickListener {
            navigateToAddDebt()
        }

        presenter.loadDebts()
    }

    override fun showDebtList(debts: List<People>) {
        adapter.peopleList.clear()
        adapter.peopleList.addAll(debts)
        refreshListView()
    }

    override fun showDeleteConfirmation(people: People, position: Int) {
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Delete Debt")
        builder.setMessage("Are you sure you want to delete ${people.name}'s debt of ₱${String.format("%.2f", people.amount)}?")
        builder.setPositiveButton("Delete") { _, _ ->
            presenter.deleteDebt(position)
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }

    override fun showDebtAdded() {
        Toast.makeText(this, "New debt added! Total: ${adapter.peopleList.size} debts", Toast.LENGTH_SHORT).show()
    }

    override fun showDebtDeleted(people: People) {
        Toast.makeText(this, "Deleted ${people.name}'s debt", Toast.LENGTH_SHORT).show()
    }

    override fun showItemClicked(people: People) {
        Toast.makeText(this, "Selected: ${people.name}", Toast.LENGTH_SHORT).show()
    }

    override fun showImageClicked(people: People) {
        Toast.makeText(this, "Image Clicked: ₱${String.format("%.2f", people.amount)}", Toast.LENGTH_SHORT).show()
    }

    override fun refreshListView() {
        adapter.notifyDataSetChanged()
    }

    override fun navigateToAddDebt() {
        val intent = Intent(this, AddDebtActivity::class.java)
        startActivityForResult(intent, ADD_DEBT_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == ADD_DEBT_REQUEST && resultCode == Activity.RESULT_OK) {
            val username = data?.getStringExtra("name") ?: ""
            val amount = data?.getDoubleExtra("amount", 0.0) ?: 0.0
            val photoRes = data?.getIntExtra("photoRes", R.drawable.android) ?: R.drawable.android
            presenter.onActivityResult(username, amount, photoRes)
        }
    }
}