package com.example.utanglog.utils

import android.app.Activity
import android.widget.Button
import android.widget.EditText
import android.widget.Toast


fun Activity.getEditTextValue(id: Int): String {
    return (findViewById<EditText>(id)).text.toString()
}

fun Activity.getButton(id:Int):Button{
    return findViewById<Button>(id)
}

fun Activity.showToast(message:String){
    Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
}