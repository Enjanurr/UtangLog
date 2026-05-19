package com.example.utanglog.data

import com.example.utanglog.R
import java.io.Serializable

data class People(
    var name: String = "",
    var amount: Double = 0.0,
    var dueDate: String = "",
    var status: String = "Pending",
    var address: String = "",
    var photoPath: String = "",  // Store image path, not drawable
    var photoRes: Int = R.drawable.ic_person_placeholder  // Default profile image
) : Serializable