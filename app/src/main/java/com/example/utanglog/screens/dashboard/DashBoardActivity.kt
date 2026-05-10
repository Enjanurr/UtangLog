package com.example.utanglog.screens.dashboard

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.utanglog.R

class DashBoardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dash_board)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        /*
        // In your RecyclerView Adapter's onBindViewHolder
when (status) {
    "Paid" -> {
        holder.statusBadge.text = "PAID"
        holder.statusBadge.setBackgroundResource(R.drawable.bg_status_paid)
        holder.statusBadge.setTextColor(ContextCompat.getColor(context, R.color.status_paid))
    }
    "Overdue" -> {
        holder.statusBadge.text = "OVERDUE"
        holder.statusBadge.setBackgroundResource(R.drawable.bg_status_overdue)
        holder.statusBadge.setTextColor(ContextCompat.getColor(context, R.color.status_overdue))
    }
    else -> {
        holder.statusBadge.text = "PENDING"
        holder.statusBadge.setBackgroundResource(R.drawable.bg_status_pending)
        holder.statusBadge.setTextColor(ContextCompat.getColor(context, R.color.status_pending))
    }
}
         */
    }
}