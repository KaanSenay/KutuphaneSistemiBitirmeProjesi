package com.example.randevusistemi

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AdminBookActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AdminAdapter
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_book)

        firestore = Firebase.firestore

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = AdminAdapter { userEmail, bookId ->""
            approvePenalty(userEmail, bookId)
        }
        recyclerView.adapter = adapter

        loadUsersWithPenalties()
    }

    private fun loadUsersWithPenalties() {
        firestore.collection("books")
            .whereEqualTo("isBorrowed", true)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val usersWithPenalties = mutableListOf<Triple<String, Int, String>>()
                for (document in querySnapshot.documents) {
                    val userId = document.getString("borrowedBy") ?: continue
                    val penalty = document.getLong("penalty")?.toInt() ?: 0
                    val bookId = document.id
                    val penaltyApproved = document.getBoolean("penaltyApproved") ?: false

                    if (!penaltyApproved) {
                        firestore.collection("users").document(userId)
                            .get()
                            .addOnSuccessListener { userDoc ->
                                val userEmail = userDoc.getString("email") ?: "Unknown"
                                usersWithPenalties.add(Triple(userEmail, penalty, bookId))
                                adapter.submitList(usersWithPenalties)
                            }
                            .addOnFailureListener {
                                Toast.makeText(this, "Failed to load user data", Toast.LENGTH_SHORT).show()
                            }
                    }
                }
                /*if (usersWithPenalties.isEmpty()) {
                    Toast.makeText(this, "No borrowed books found", Toast.LENGTH_SHORT).show()
                }*/
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load users with penalties", Toast.LENGTH_SHORT).show()
            }
    }

    private fun approvePenalty(userEmail: String, bookId: String) {
        firestore.runTransaction { transaction ->
            val bookRef = firestore.collection("books").document(bookId)
            val bookSnapshot = transaction.get(bookRef)

            if (bookSnapshot.getBoolean("isBorrowed") == true) {
                transaction.update(bookRef, "penalty", 0)
                transaction.update(bookRef, "penaltyApproved", true)
            }
        }.addOnSuccessListener {
            Toast.makeText(this, "Penalty approved", Toast.LENGTH_SHORT).show()
            loadUsersWithPenalties()
            finish()
            overridePendingTransition(0, 0)
            startActivity(getIntent())
            overridePendingTransition(0, 0)
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to approve penalty", Toast.LENGTH_SHORT).show()
        }
    }
}
