package com.example.randevusistemi

import BookAdapter
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class BookListActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: BookAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_list)

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = BookAdapter { book ->
            val intent = Intent(this, BookDetailActivity::class.java)
            intent.putExtra("bookId", book.id)
            println(book.id)
            startActivity(intent)
        }
        recyclerView.adapter = adapter

        loadBooks()
    }

    private fun loadBooks() {
        FirebaseFirestore.getInstance().collection("books")
            .addSnapshotListener { snapshot, e ->
                if (e != null) return@addSnapshotListener
                val books = snapshot?.mapNotNull { it.toObject(Book::class.java).also { book -> book.id = it.id } }.orEmpty()
                adapter.submitList(books)
            }
    }
}
