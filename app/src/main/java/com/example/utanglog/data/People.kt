package com.example.utanglog.data

import com.example.utanglog.R

data class People(
    var name: String = "",
    var amount: Double = 0.0,
    var dueDate: String = "",
    var status: String = "Pending",
    var address: String = "",
    var photoRes: Int = R.drawable.ic_person_placeholder
)