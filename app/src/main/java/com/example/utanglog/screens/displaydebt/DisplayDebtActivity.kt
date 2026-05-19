package com.example.utanglog.screens.displaydebt

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.utanglog.R
import com.example.utanglog.screens.debtdetail.DebtDetailActivity
import com.example.utanglog.screens.profile.ProfileActivity
import com.example.utanglog.data.People
import com.example.utanglog.helper.DebtRecyclerViewAdapter
import com.example.utanglog.screens.adddebt.AddDebtActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class DisplayDebtActivity : Activity(), DisplayDebtContract.View {

    private lateinit var presenter: DisplayDebtPresenter
    private lateinit var adapter: DebtRecyclerViewAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyStateLayout: LinearLayout
    private lateinit var textviewTotalAmount: TextView
    private lateinit var textviewDebtCount: TextView

    private val ADD_DEBT_REQUEST = 1
    private val DEBT_DETAIL_REQUEST = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_debt)

        presenter = DisplayDebtPresenter(this, DisplayDebtModel())

        recyclerView = findViewById(R.id.recyclerView)
        emptyStateLayout = findViewById(R.id.emptyStateLayout)
        textviewTotalAmount = findViewById(R.id.textviewTotalAmount)
        textviewDebtCount = findViewById(R.id.textviewDebtCount)

        setupBottomNavigation()

        // Setup RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = DebtRecyclerViewAdapter(
            this,
            mutableListOf(),
            onItemClick = { people, position ->
                val intent = Intent(this, DebtDetailActivity::class.java).apply {
                    putExtra("people", people)
                    putExtra("position", position)
                }
                startActivityForResult(intent, DEBT_DETAIL_REQUEST)
            }
        )
        recyclerView.adapter = adapter

        // Load debts
        presenter.loadDebts()
    }

    private fun setupBottomNavigation() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNav.selectedItemId = R.id.nav_home
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    true
                }
                R.id.nav_add -> {
                    startActivityForResult(Intent(this, AddDebtActivity::class.java), ADD_DEBT_REQUEST)
                    true
                }
                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }
    }

    override fun showDebtList(debts: List<People>) {
        adapter.updateList(debts)
        updateSummary(debts)

        // Show/Hide empty state based on list size
        if (debts.isEmpty()) {
            recyclerView.visibility = android.view.View.GONE
            emptyStateLayout.visibility = android.view.View.VISIBLE
        } else {
            recyclerView.visibility = android.view.View.VISIBLE
            emptyStateLayout.visibility = android.view.View.GONE
        }
    }

    private fun updateSummary(debts: List<People>) {
        val total = debts.sumOf { it.amount }
        textviewTotalAmount.text = String.format("₱%.2f", total)
        textviewDebtCount.text = "${debts.size} ${if (debts.size == 1) "debt" else "debts"}"
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
        // Handled by bottom nav
    }

    override fun updateSummary(total: Double, count: Int) {
        textviewTotalAmount.text = String.format("₱%.2f", total)
        textviewDebtCount.text = "$count ${if (count == 1) "debt" else "debts"}"
    }

    override fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ADD_DEBT_REQUEST && resultCode == Activity.RESULT_OK) {
            val name = data?.getStringExtra("name") ?: ""
            val amount = data?.getDoubleExtra("amount", 0.0) ?: 0.0
            val status = data?.getStringExtra("status") ?: "Pending"
            val dueDate = data?.getStringExtra("dueDate") ?: ""
            val address = data?.getStringExtra("address") ?: ""
            val photoPath = data?.getStringExtra("photoPath") ?: ""

            presenter.onActivityResult(name, amount, status, dueDate, address, photoPath)
        }
        else if (requestCode == DEBT_DETAIL_REQUEST && resultCode == Activity.RESULT_OK) {
            val updatePosition = data?.getIntExtra("update_position", -1) ?: -1
            if (updatePosition != -1) {
                val updatedPeople = People(
                    name = data?.getStringExtra("updated_name") ?: "",
                    amount = data?.getDoubleExtra("updated_amount", 0.0) ?: 0.0,
                    dueDate = data?.getStringExtra("updated_dueDate") ?: "",
                    status = data?.getStringExtra("updated_status") ?: "Pending",
                    address = data?.getStringExtra("updated_address") ?: "",
                    photoPath = data?.getStringExtra("updated_photoPath") ?: ""
                )
                presenter.updateDebt(updatedPeople, updatePosition)
            }

            val deletePosition = data?.getIntExtra("delete_position", -1) ?: -1
            if (deletePosition != -1) {
                presenter.deleteDebt(deletePosition)
            }
        }
    }
}