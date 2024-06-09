package com.example.randevusistemi

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.Calendar
import java.util.Date

class BookDetailActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_detail)

        val tvBookTitle = findViewById<TextView>(R.id.tvBookTitle)
        val tvBookDescription = findViewById<TextView>(R.id.tvBookDescription)
        val btnBorrowBook = findViewById<Button>(R.id.btnBorrowBook)
        val bookId = intent.getStringExtra("bookId")!!

        auth = Firebase.auth
        firestore = Firebase.firestore

        FirebaseFirestore.getInstance().collection("books").document(bookId)
            .get()
            .addOnSuccessListener { document ->
                tvBookTitle.text = document.getString("title")
                tvBookDescription.text = document.getString("description")
                btnBorrowBook.isEnabled = document.getBoolean("isBorrowed") != true
            }

        btnBorrowBook.setOnClickListener {
            borrowBook(bookId)
        }

    }

    private fun borrowBook(bookId: String) {
        //val currentUser = FirebaseAuth.getInstance().currentUser
        //val currentUserID = currentUser!!.uid  // Kullanıcının unique ID'si
        //val currentUserID = auth  // Kullanıcının unique ID'si
        val user = FirebaseAuth.getInstance().currentUser
        val db = FirebaseFirestore.getInstance()
        val bookRef = db.collection("books").document(bookId)
        val userRef = db.collection("users").document(auth.currentUser!!.uid)
        println(auth.currentUser!!.email.toString())
        //val userRef = db.collection("users").document(currentUserID)
        val borrowDate = Date()

        db.runTransaction { transaction ->
            val bookSnapshot = transaction.get(bookRef)
            if (bookSnapshot.getBoolean("isBorrowed") == true) {
                throw Exception("This book has already been borrowed.")
            }
            val dueDate = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, 14) }.time

            transaction.update(bookRef, mapOf(
                "isBorrowed" to true,
                "borrowedBy" to auth.currentUser!!.uid,
                "borrowDate" to borrowDate,
                "dueDate" to dueDate
            ))
            transaction.update(userRef, "borrowedBooks", FieldValue.arrayUnion(bookId))
            null
        }.addOnSuccessListener {
            finish()
        }.addOnFailureListener {
            // Hata yönetimi
            Log.e("Firestore Error", "Transaction failed: $it")
        }
    }
}
