package com.example.utanglog.screens.displayDebt

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.example.utanglog.R
import com.example.utanglog.screens.addDebt.AddDebtActivity
import com.example.utanglog.data.People
import com.example.utanglog.helper.DebtAdapter

class DisplayDebtActivity : Activity(), DisplayDebtContract.View {

    private lateinit var presenter: DisplayDebtPresenter
    private lateinit var adapter: DebtAdapter
    private lateinit var listView: ListView
    private lateinit var tvTotalAmount: TextView
    private lateinit var tvDebtCount: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_debt)

        presenter = DisplayDebtPresenter(this, DisplayDebtModel())

        listView = findViewById(R.id.listView)
        tvTotalAmount = findViewById(R.id.tv_total_amount)
        tvDebtCount = findViewById(R.id.tv_debt_count)
        val btnAddDebt = findViewById<Button>(R.id.btn_add_debt)
        val backArrow = findViewById<ImageView>(R.id.back_arrow)

        adapter = DebtAdapter(
            this,
            mutableListOf(),
            onItemClick = { people -> presenter.onItemClick(people) }
        )
        listView.adapter = adapter

        btnAddDebt.setOnClickListener {
            navigateToAddDebt()
        }

        backArrow.setOnClickListener {
            finish()
        }

        // Check if coming from AddDebtActivity with new data
        if (intent.hasExtra("name")) {
            val name = intent.getStringExtra("name") ?: ""
            val amount = intent.getDoubleExtra("amount", 0.0)
            val status = intent.getStringExtra("status") ?: "Pending"
            val dueDate = intent.getStringExtra("dueDate") ?: ""
            val address = intent.getStringExtra("address") ?: ""

            if (name.isNotEmpty() && amount > 0.0) {
                // Add to existing list (not replace)
                presenter.addDebtFromIntent(name, amount, status, dueDate, address)
            }
        }

        presenter.loadDebts()
    }

    override fun showDebtList(debts: List<People>) {
        adapter.updateList(debts)
        updateSummary(debts)
    }

    private fun updateSummary(debts: List<People>) {
        val total = debts.sumOf { it.amount }
        tvTotalAmount.text = String.format("₱%.2f", total)
        tvDebtCount.text = "${debts.size} ${if (debts.size == 1) "debt" else "debts"}"
    }

    override fun showDebtAdded() {
        Toast.makeText(this, "New debt added!", Toast.LENGTH_SHORT).show()
    }

    override fun showItemClicked(people: People) {
        Toast.makeText(this, "${people.name} owes ₱${String.format("%.2f", people.amount)}", Toast.LENGTH_LONG).show()
    }

    override fun refreshListView() {
        adapter.notifyDataSetChanged()
    }

    override fun navigateToAddDebt() {
        val intent = Intent(this, AddDebtActivity::class.java)
        startActivity(intent)
    }

    override fun updateSummary(total: Double, count: Int) {
        tvTotalAmount.text = String.format("₱%.2f", total)
        tvDebtCount.text = "$count ${if (count == 1) "debt" else "debts"}"
    }
}