package com.example.randevusistemi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class TablesAndUsers : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TableAdapter
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tables_and_users)

        firestore = Firebase.firestore

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = TableAdapter()
        recyclerView.adapter = adapter

        loadOccupiedTables()
    }

    private fun loadOccupiedTables() {
        firestore.collection("tables")
            .whereEqualTo("isOccupied", true)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val tables = mutableListOf<Table>()
                for (document in querySnapshot.documents) {
                    val name = document.id
                    val occupiedBy = document.getString("occupiedBy") ?: continue
                    tables.add(Table(name, occupiedBy))
                }
                adapter.submitList(tables)
            }
            .addOnFailureListener { e ->
                Log.e("TablesAndUsers", "Doluluk durumları alınamadı: ${e.message}")
                Toast.makeText(this, "Failed to load occupied tables", Toast.LENGTH_SHORT).show()
            }
    }
}
