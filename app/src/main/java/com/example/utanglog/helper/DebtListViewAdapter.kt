package com.example.utanglog.helper

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.*
import com.example.utanglog.R
import com.example.utanglog.data.People

class  DebtListViewAdapter(
    private val context: Context,
    val peopleList: MutableList<People>,
    private val onImageClick: (People) -> Unit,
    private val onItemClick: (People) -> Unit,
    private val onDeleteClick: (People, Int) -> Unit
) : BaseAdapter() {

    override fun getCount(): Int = peopleList.size

    override fun getItem(position: Int): Any = peopleList[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item, parent, false)

        val profile = view.findViewById<ImageView>(R.id.picture)
        val name = view.findViewById<TextView>(R.id.textUsername)
        val debtAmount = view.findViewById<TextView>(R.id.textAmount)
        val deleteButton = view.findViewById<Button>(R.id.deleteButton)

        val people = peopleList[position]

        profile.setImageResource(people.photoRes)
        name.text = people.name
        // Format Double to currency with 2 decimal places
        debtAmount.text = String.format("₱%.2f", people.amount)

        profile.setOnClickListener {
            onImageClick(people)
        }

        view.setOnClickListener {
            onItemClick(people)
        }

        deleteButton.setOnClickListener {
            onDeleteClick(people, position)
        }

        return view
    }
}