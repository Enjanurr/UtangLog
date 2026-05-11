package com.example.utanglog.helper

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.utanglog.R
import com.example.utanglog.data.People

class DebtAdapter(
    private val context: Context,
    private var debtList: MutableList<People>,
    private val onItemClick: (People) -> Unit
) : BaseAdapter() {

    override fun getCount(): Int = debtList.size

    override fun getItem(position: Int): Any = debtList[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_debt, parent, false)

        val ivAvatar = view.findViewById<ImageView>(R.id.iv_avatar)
        val tvName = view.findViewById<TextView>(R.id.tv_name)
        val tvDueDate = view.findViewById<TextView>(R.id.tv_due_date)
        val tvAmount = view.findViewById<TextView>(R.id.tv_amount)

        val debt = debtList[position]

        ivAvatar.setImageResource(debt.photoRes)
        tvName.text = debt.name
        tvDueDate.text = "Due: ${debt.dueDate}"  // You'll add dueDate to People class
        tvAmount.text = String.format("₱%.2f", debt.amount)

        view.setOnClickListener {
            onItemClick(debt)
        }

        return view
    }

    fun updateList(newList: List<People>) {
        debtList.clear()
        debtList.addAll(newList)
        notifyDataSetChanged()
    }
}