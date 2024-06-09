package com.example.randevusistemi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class User(val id: String, val email: String)

class UserAdapter(private val onUserClick: (User) -> Unit) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    private var users: List<User> = emptyList()

    fun submitList(newUsers: List<User>) {
        users = newUsers
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view, onUserClick)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(users[position])
    }

    override fun getItemCount() = users.size

    class UserViewHolder(itemView: View, private val onUserClick: (User) -> Unit) : RecyclerView.ViewHolder(itemView) {
        private val emailView: TextView = itemView.findViewById(R.id.user_email)

        fun bind(user: User) {
            emailView.text = user.email
            itemView.setOnClickListener { onUserClick(user) }
        }
    }
}
