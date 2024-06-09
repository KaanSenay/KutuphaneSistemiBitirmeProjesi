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
import com.example.randevusistemi.databinding.ActivityMainAppBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainApp : AppCompatActivity() {
    private lateinit var binding: ActivityMainAppBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainAppBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = Firebase.auth
        firestore = Firebase.firestore

        val buttonIds = listOf(R.id.masa1, R.id.masa2, R.id.masa3, R.id.masa4, R.id.masa5, R.id.masa6, R.id.masa7, R.id.masa8, R.id.masa9)

        handler = Handler(Looper.getMainLooper())
        runnable = object : Runnable {
            override fun run() {
                // Masaların doluluk durumunu kontrol et
                buttonIds.forEachIndexed { index, buttonId ->
                    val button = findViewById<Button>(buttonId)
                    checkTableStatus("masa${index + 1}", button)
                }
                // 2 saniye sonra tekrar çalıştır
                handler.postDelayed(this, 2000)
            }
        }
        handler.post(runnable)

        for (i in 1..9) {
            var button = findViewById<Button>(buttonIds[i - 1])
            button.setOnClickListener{
                buttonclicked(it,"masa$i")
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
        val docRef = firestore.collection("tables").document(masaNo)
        docRef.get().addOnSuccessListener { document ->
            if (document.getBoolean("isOccupied") == true) {
                Toast.makeText(this,"HATA", Toast.LENGTH_LONG).show()
            } else {
                // Eğer masada kimse yoksa, masa otur
                docRef.update(mapOf(
                    "isOccupied" to true,
                    "occupiedBy" to auth.currentUser?.uid
                )).addOnSuccessListener {
                    Toast.makeText(this, "$masaNo numaralı masaya oturdunuz", Toast.LENGTH_LONG).show()
                }
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Masanın durumunu güncellerken bir hata oluştu", Toast.LENGTH_LONG).show()
        }
    }*/

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