package com.example.utanglog.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.utanglog.R
import com.example.utanglog.data.People
import kotlin.math.min

class DebtRecyclerViewAdapter(
    private val context: Context,
    private var debtList: List<People>,
    private val onItemClick: (People, Int) -> Unit
) : RecyclerView.Adapter<DebtRecyclerViewAdapter.DebtViewHolder>() {

    inner class DebtViewHolder(itemView: android.view.View) : RecyclerView.ViewHolder(itemView) {
        val imageviewAvatar: ImageView = itemView.findViewById(R.id.imageviewAvatar)
        val textviewName: TextView = itemView.findViewById(R.id.textviewName)
        val textviewDueDate: TextView = itemView.findViewById(R.id.textviewDueDate)
        val textviewAmount: TextView = itemView.findViewById(R.id.textviewAmount)
        val textviewAddress: TextView = itemView.findViewById(R.id.textviewAddress)
        val textviewStatus: TextView = itemView.findViewById(R.id.textviewStatus)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(debtList[position], position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DebtViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_debt, parent, false)
        return DebtViewHolder(view)
    }

    override fun onBindViewHolder(holder: DebtViewHolder, position: Int) {
        val debt = debtList[position]

        // Load circular image from path
        if (debt.photoPath.isNotEmpty()) {
            try {
                val originalBitmap = BitmapFactory.decodeFile(debt.photoPath)
                if (originalBitmap != null) {
                    val circularBitmap = getCircularBitmap(originalBitmap)
                    holder.imageviewAvatar.setImageBitmap(circularBitmap)
                } else {
                    holder.imageviewAvatar.setImageResource(R.drawable.ic_person_placeholder)
                }
            } catch (e: Exception) {
                holder.imageviewAvatar.setImageResource(R.drawable.ic_person_placeholder)
            }
        } else {
            holder.imageviewAvatar.setImageResource(R.drawable.ic_person_placeholder)
        }

        holder.textviewName.text = debt.name
        holder.textviewDueDate.text = "Due: ${debt.dueDate}"
        holder.textviewAmount.text = String.format("₱%.2f", debt.amount)
        holder.textviewAddress.text = debt.address
        holder.textviewStatus.text = debt.status

        when (debt.status) {
            "Paid" -> {
                holder.textviewStatus.background = ContextCompat.getDrawable(context, R.drawable.bg_status_paid)
                holder.textviewStatus.setTextColor(ContextCompat.getColor(context, R.color.status_paid_text))
            }
            "Overdue" -> {
                holder.textviewStatus.background = ContextCompat.getDrawable(context, R.drawable.bg_status_overdue)
                holder.textviewStatus.setTextColor(ContextCompat.getColor(context, R.color.status_overdue_text))
            }
            else -> {
                holder.textviewStatus.background = ContextCompat.getDrawable(context, R.drawable.bg_status_pending)
                holder.textviewStatus.setTextColor(ContextCompat.getColor(context, R.color.status_pending_text))
            }
        }
    }

    private fun getCircularBitmap(bitmap: Bitmap): Bitmap {
        val size = min(bitmap.width, bitmap.height)
        val output = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)

        val paint = Paint()
        paint.isAntiAlias = true

        val rect = Rect(0, 0, size, size)
        val srcRect = Rect(
            (bitmap.width - size) / 2,
            (bitmap.height - size) / 2,
            (bitmap.width + size) / 2,
            (bitmap.height + size) / 2
        )

        canvas.drawCircle(size / 2f, size / 2f, size / 2f, paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, srcRect, rect, paint)

        return output
    }

    override fun getItemCount(): Int = debtList.size

    fun updateList(newList: List<People>) {
        debtList = newList
        notifyDataSetChanged()
    }
}