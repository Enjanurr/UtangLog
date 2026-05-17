package com.example.utanglog.helper

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.utanglog.R
import com.example.utanglog.data.People

class DebtAdapter(
    private val context: Context,
    val debtList: MutableList<People>,
    private val onItemClick: (People, Int) -> Unit
) : BaseAdapter() {

    override fun getCount(): Int = debtList.size

    override fun getItem(position: Int): Any = debtList[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_debt, parent, false)

        val imageviewAvatar = view.findViewById<ImageView>(R.id.imageviewAvatar)
        val textviewName = view.findViewById<TextView>(R.id.textviewName)
        val textviewDueDate = view.findViewById<TextView>(R.id.textviewDueDate)
        val textviewAmount = view.findViewById<TextView>(R.id.textviewAmount)
        val textviewAddress = view.findViewById<TextView>(R.id.textviewAddress)
        val textviewStatus = view.findViewById<TextView>(R.id.textviewStatus)

        val debt = debtList[position]

        // Load image from drawable resource
        imageviewAvatar.setImageResource(debt.photoRes)

        textviewName.text = debt.name
        textviewDueDate.text = "Due: ${debt.dueDate}"
        textviewAmount.text = String.format("₱%.2f", debt.amount)
        textviewAddress.text = debt.address
        textviewStatus.text = debt.status

        // Set Status Color
        when (debt.status) {
            "Paid" -> {
                textviewStatus.background = ContextCompat.getDrawable(context, R.drawable.bg_status_paid)
                textviewStatus.setTextColor(ContextCompat.getColor(context, R.color.status_paid_text))
            }
            "Overdue" -> {
                textviewStatus.background = ContextCompat.getDrawable(context, R.drawable.bg_status_overdue)
                textviewStatus.setTextColor(ContextCompat.getColor(context, R.color.status_overdue_text))
            }
            else -> {
                textviewStatus.background = ContextCompat.getDrawable(context, R.drawable.bg_status_pending)
                textviewStatus.setTextColor(ContextCompat.getColor(context, R.color.status_pending_text))
            }
        }

        view.setOnClickListener {
            onItemClick(debt, position)
        }

        return view
    }
    fun updateList(newList: List<People>) {
        debtList.clear()
        debtList.addAll(newList)
        notifyDataSetChanged()
    }
}