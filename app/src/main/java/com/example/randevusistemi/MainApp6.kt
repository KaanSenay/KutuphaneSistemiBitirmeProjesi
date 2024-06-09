package com.example.randevusistemi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.randevusistemi.databinding.ActivityMainApp6Binding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainApp6 : AppCompatActivity() {
    private lateinit var binding: ActivityMainApp6Binding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainApp6Binding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = Firebase.auth
        firestore = Firebase.firestore

        val buttonIds = listOf(R.id.masa46, R.id.masa47, R.id.masa48, R.id.masa49, R.id.masa50, R.id.masa51, R.id.masa52, R.id.masa53, R.id.masa54)

        handler = Handler(Looper.getMainLooper())
        runnable = object : Runnable {
            override fun run() {
                // Masaların doluluk durumunu kontrol et
                buttonIds.forEachIndexed { index, buttonId ->
                    val button = findViewById<Button>(buttonId)
                    checkTableStatus("masa${index + 46}", button)
                }
                // 2 saniye sonra tekrar çalıştır
                handler.postDelayed(this, 2000)
            }
        }
        handler.post(runnable)

        for (i in 1..9) {
            var button = findViewById<Button>(buttonIds[i - 1])
            button.setOnClickListener{
                buttonclicked(it,"masa${45 + i}")
            }
        }

    }

    fun checkTableStatus(tableName: String, button: Button) {
        firestore.collection("tables").document(tableName)
            .get()
            .addOnSuccessListener { document ->
                val isOccupied = document.getBoolean("isOccupied") ?: false
                button.isEnabled = !isOccupied
            }
    }

    fun buttonclicked(view: View, masaNo: String) {
        val userRef = firestore.collection("users").document(auth.currentUser!!.uid)
        userRef.get().addOnSuccessListener { doc ->
            if (doc.getString("currentTable") != null && doc.getString("currentTable") != masaNo) {
                Toast.makeText(this, "You are already seated at a table!", Toast.LENGTH_LONG).show()
            } else {
                // Masa durumunu güncelle
                val docRef = firestore.collection("tables").document(masaNo)
                docRef.get().addOnSuccessListener { document ->
                    if (document.getBoolean("isOccupied") == true) {
                        Toast.makeText(this, "This table is occupied!", Toast.LENGTH_LONG).show()
                    } else {
                        /*docRef.update("isOccupied" to true, "occupiedBy" to auth.currentUser?.uid)
                        userRef.update("currentTable" to masaNo)*/
                        docRef.update("isOccupied", true).addOnSuccessListener {
                            docRef.update("occupiedBy", auth.currentUser!!.uid).addOnSuccessListener {
                                userRef.update("currentTable", masaNo).addOnSuccessListener {
                                    Toast.makeText(this, "You have taken a seat at the table", Toast.LENGTH_LONG).show()
                                }
                            }
                        }

                    }
                }
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Masanın durumunu güncellerken bir hata oluştu", Toast.LENGTH_LONG).show()
        }
    }

    /*fun buttonclicked(view: View, masaNo: String) {
        firestore.collection("tables").document(masaNo)
            .update("isOccupied", true)
            .addOnSuccessListener {
                println("başarılı")
                Toast.makeText(this, "işlem başarılı", Toast.LENGTH_LONG).show()
                //recreate() bastığında aktiviteyi yeniden başlatmak için bu fonksiyon yanlış hatırlamıyorsam API 11 üzeri çalışıyor
            }
            .addOnFailureListener {
                println("hata")
                Toast.makeText(this, "Masanın durumunu güncellerken bir hata oluştu", Toast.LENGTH_LONG).show()
            }
    }*/

    /*override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.menu,menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.logout) {
            auth.signOut()
            val intent = Intent(this,Login::class.java)
            startActivity(intent)
            finish()
        }

        return super.onOptionsItemSelected(item)
    }*/

}