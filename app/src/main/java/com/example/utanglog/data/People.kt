package com.example.utanglog.data

import com.example.utanglog.R

data class People (
    var name: String = "",
    var amount: Double = 0.0,  // Changed from String to Double
    var photoRes: Int = R.drawable.android
)