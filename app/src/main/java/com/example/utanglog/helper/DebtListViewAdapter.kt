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

class DebtListViewAdapter(
    private val context: Context,
    val peopleList: MutableList<People>,
    private val onItemClick: (People) -> Unit
) : BaseAdapter() {

    override fun getCount(): Int = peopleList.size

    override fun getItem(position: Int): Any = peopleList[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_debt, parent, false)  // Use item_debt.xml

        val profile = view.findViewById<ImageView>(R.id.picture)
        val name = view.findViewById<TextView>(R.id.textUsername)
        val debtAmount = view.findViewById<TextView>(R.id.textAmount)

        val people = peopleList[position]

        //profile.setImageResource(people.photoRes)
        name.text = people.name
        debtAmount.text = String.format("₱%.2f", people.amount)

        view.setOnClickListener {
            onItemClick(people)
        }

        return view
    }
}