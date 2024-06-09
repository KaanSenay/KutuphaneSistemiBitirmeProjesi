package com.example.randevusistemi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdminAdapter(private val onApproveClick: (String, String) -> Unit) : RecyclerView.Adapter<AdminAdapter.AdminViewHolder>() {
    private var usersWithPenalties: MutableList<Triple<String, Int, String>> = mutableListOf()

    fun submitList(newUsersWithPenalties: List<Triple<String, Int, String>>) {
        usersWithPenalties.clear()
        usersWithPenalties.addAll(newUsersWithPenalties)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user_penalty, parent, false)
        return AdminViewHolder(view, onApproveClick)
    }

    override fun onBindViewHolder(holder: AdminViewHolder, position: Int) {
        holder.bind(usersWithPenalties[position])
    }

    override fun getItemCount() = usersWithPenalties.size

    class AdminViewHolder(itemView: View, private val onApproveClick: (String, String) -> Unit) : RecyclerView.ViewHolder(itemView) {
        private val userEmailView: TextView = itemView.findViewById(R.id.user_email)
        private val penaltyView: TextView = itemView.findViewById(R.id.penalty)
        private val approveButton: Button = itemView.findViewById(R.id.approve_button)

        fun bind(userWithPenalty: Triple<String, Int, String>) {
            val (userEmail, penalty, bookId) = userWithPenalty
            userEmailView.text = "Email: $userEmail"
            penaltyView.text = "Penalty: $penalty TL"
            approveButton.setOnClickListener {
                onApproveClick(userEmail, bookId)
            }
        }
    }
}
