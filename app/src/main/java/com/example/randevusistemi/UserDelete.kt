package com.example.randevusistemi

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UserDelete : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: UserAdapter
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_delete)

        firestore = Firebase.firestore

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = UserAdapter { user ->
            deleteUser(user.id)
        }
        recyclerView.adapter = adapter

        loadUsers()
    }

    private fun loadUsers() {
        firestore.collection("users")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val users = querySnapshot.documents.map { document ->
                    User(document.id, document.getString("email") ?: "Unknown")
                }
                adapter.submitList(users)
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to load users", Toast.LENGTH_SHORT).show()
            }
    }

    private fun deleteUser(userId: String) {
        firestore.collection("users").document(userId)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(this, "User deleted", Toast.LENGTH_SHORT).show()
                loadUsers() // RecyclerView'u güncelle
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to delete user", Toast.LENGTH_SHORT).show()
            }
    }
}
