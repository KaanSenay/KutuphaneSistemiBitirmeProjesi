package com.example.randevusistemi

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.util.*

class ReturnBooksActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ReturnBookAdapter
    private lateinit var auth: FirebaseAuth
    private lateinit var totalPenaltyView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_return_books)

        auth = Firebase.auth

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ReturnBookAdapter { book ->
            returnBook(book)
        }
        recyclerView.adapter = adapter

        totalPenaltyView = findViewById(R.id.total_penalty)

        loadBorrowedBooks()
        loadTotalPenalty()
    }

    private fun loadBorrowedBooks() {
        val currentUserID = auth.currentUser!!.uid
        FirebaseFirestore.getInstance().collection("users").document(currentUserID)
            .get()
            .addOnSuccessListener { document ->
                val bookIds = document.get("borrowedBooks") as? List<String> ?: listOf()
                if (bookIds.isNotEmpty()) {
                    FirebaseFirestore.getInstance().collection("books")
                        .whereIn(FieldPath.documentId(), bookIds)
                        .get()
                        .addOnSuccessListener { querySnapshot ->
                            val books = querySnapshot.map { it.toObject(Book::class.java).also { book -> book.id = it.id } }
                            adapter.submitList(books)
                        }
                } else {
                    Toast.makeText(this, "No books have been borrowed.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun loadTotalPenalty() {
        val currentUserID = auth.currentUser!!.uid
        FirebaseFirestore.getInstance().collection("books")
            .whereEqualTo("borrowedBy", currentUserID)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val totalPenalty = querySnapshot.sumBy { it.getLong("penalty")?.toInt() ?: 0 }
                totalPenaltyView.text = "Current Debt: $totalPenalty TL"
            }
    }

    private fun returnBook(book: Book) {
        val currentUserID = auth.currentUser!!.uid
        val db = FirebaseFirestore.getInstance()
        val bookRef = db.collection("books").document(book.id)
        val userRef = db.collection("users").document(currentUserID)
        val returnDate = Date()

        db.runTransaction { transaction ->
            val bookSnapshot = transaction.get(bookRef)

            // Borç onayı kontrolü
            if (bookSnapshot.getBoolean("penaltyApproved") != true) {
                throw Exception("Cannot be returned without debt approval.")
            }

            val dueDate = bookSnapshot.getDate("dueDate")!!
            val overdueDays = ((returnDate.time - dueDate.time) / (1000 * 60 * 60 * 24)).toInt()
            val fine = if (overdueDays > 0) overdueDays * 5 else 0

            // Kitap belgesine ceza ekleme
            transaction.update(bookRef, "penalty", fine)

            // Kitap durumu güncelleme
            transaction.update(bookRef, mapOf(
                "isBorrowed" to false,
                "borrowedBy" to null,
                "borrowDate" to null,
                "dueDate" to null,
                "penalty" to 0,  // İade edildikten sonra cezayı sıfırlayabiliriz
                "penaltyApproved" to false  // Borç onayı sıfırlanır
            ))
            transaction.update(userRef, "borrowedBooks", FieldValue.arrayRemove(book.id))
            fine
        }.addOnSuccessListener { fine ->
            if (fine > 0) {
                Toast.makeText(this, "You have a fine of $fine.", Toast.LENGTH_SHORT).show()
            }
            loadTotalPenalty()  // Toplam borcu güncelle
            val intent = Intent(this, Kitap::class.java)
            startActivity(intent)
            finish()
        }.addOnFailureListener { e ->
            Toast.makeText(this, e.message ?: "Error returning book.", Toast.LENGTH_SHORT).show()
        }
    }
}
