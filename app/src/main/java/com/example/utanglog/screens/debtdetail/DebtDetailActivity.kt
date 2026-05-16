package com.example.utanglog.screens.debtdetail

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.utanglog.R
import com.example.utanglog.data.People

class DebtDetailActivity : Activity(), DebtDetailContract.View {

    private lateinit var presenter: DebtDetailPresenter
    private lateinit var currentPeople: People
    private var currentPosition: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_debt_detail)

        presenter = DebtDetailPresenter(this, DebtDetailModel())

        // Get data from intent
        currentPeople = intent.getSerializableExtra("people") as People
        currentPosition = intent.getIntExtra("position", -1)

        setupViews()
        presenter.loadDebtDetails(currentPeople)
    }

    private fun setupViews() {
        findViewById<ImageView>(R.id.back_arrow).setOnClickListener {
            finish()
        }

        findViewById<Button>(R.id.btn_edit).setOnClickListener {
            presenter.onEditClick(currentPeople)
        }

        findViewById<Button>(R.id.btn_delete).setOnClickListener {
            presenter.onDeleteClick(currentPeople, currentPosition)
        }
    }

    override fun showDebtDetails(people: People) {
        findViewById<TextView>(R.id.tv_name).text = people.name
        findViewById<TextView>(R.id.tv_amount).text = String.format("₱%.2f", people.amount)
        findViewById<TextView>(R.id.tv_due_date).text = people.dueDate
        findViewById<TextView>(R.id.tv_address).text = people.address

        val tvStatus = findViewById<TextView>(R.id.tv_status)
        tvStatus.text = people.status

        // Set status badge color
        when (people.status) {
            "Paid" -> {
                tvStatus.setBackgroundColor(ContextCompat.getColor(this, R.color.status_paid_bg))
                tvStatus.setTextColor(ContextCompat.getColor(this, R.color.status_paid))
            }
            "Overdue" -> {
                tvStatus.setBackgroundColor(ContextCompat.getColor(this, R.color.status_overdue_bg))
                tvStatus.setTextColor(ContextCompat.getColor(this, R.color.status_overdue))
            }
            else -> {
                tvStatus.setBackgroundColor(ContextCompat.getColor(this, R.color.status_pending_bg))
                tvStatus.setTextColor(ContextCompat.getColor(this, R.color.status_pending))
            }
        }
    }

    override fun showDeleteConfirmation() {
        android.app.AlertDialog.Builder(this)
            .setTitle("Delete Debt")
            .setMessage("Are you sure you want to delete ${currentPeople.name}'s debt?")
            .setPositiveButton("Delete") { _, _ ->
                val resultIntent = Intent().apply {
                    putExtra("delete_position", currentPosition)
                }
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun showEditScreen(people: People) {
        // Will implement later for UPDATE operation
        showMessage("Edit feature coming soon!")
    }

    override fun closeScreen() {
        finish()
    }

    override fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}