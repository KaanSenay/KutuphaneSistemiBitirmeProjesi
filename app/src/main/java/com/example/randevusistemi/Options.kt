package com.example.randevusistemi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import com.example.randevusistemi.databinding.ActivityOptionsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Options : AppCompatActivity() {

    private lateinit var binding: ActivityOptionsBinding
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOptionsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = Firebase.auth
        firestore = Firebase.firestore

        toggle = ActionBarDrawerToggle(this,binding.homeDrawerLayout,R.string.navigation_drawer_open,R.string.navigation_drawer_close)

        binding.bottomNavigation.background = null

        binding.bottomNavigation.selectedItemId = R.id.bottom_options

        binding.bottomNavigation.setOnItemSelectedListener{item ->
            when (item.itemId) {
                R.id.bottom_home -> {
                    val intent = Intent(this, Home::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.animate_swipe_right_enter,R.anim.animate_swipe_right_exit)
                    finish()}
                R.id.bottom_rooms -> {
                    val intent = Intent(this, OdalarActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.animate_swipe_right_enter,R.anim.animate_swipe_right_exit)
                    finish()}
                R.id.bottom_pomodoro -> {
                    val intent = Intent(this, Pomodoro::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.animate_swipe_right_enter,R.anim.animate_swipe_right_exit)
                    finish()}
                R.id.bottom_borrow -> {
                    val intent = Intent(this, Kitap::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.animate_swipe_right_enter,R.anim.animate_swipe_right_exit)
                    finish()}
                R.id.bottom_options -> {
                    /*val intent = Intent(this, Options::class.java)
                    startActivity(intent)
                    finish()*/
                    Toast.makeText(this,"You are already on this page!!", Toast.LENGTH_LONG).show()}
            }
            true
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            return true
        } else {
            return super.onOptionsItemSelected(item)
        }
    }

    fun leaveTable(view: View){
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val userRef = firestore.collection("users").document(currentUser.uid)
            userRef.get().addOnSuccessListener { document ->
                val currentTable = document.getString("currentTable")
                if (currentTable != null) {
                    val tableRef = firestore.collection("tables").document(currentTable)
                    // Kullanıcıyı masadan kaldır
                    userRef.update("currentTable", null)
                    // Masa durumunu güncelle
                    tableRef.update("isOccupied", false, "occupiedBy", null).addOnSuccessListener {
                        Toast.makeText(this,"You have left the table.",Toast.LENGTH_LONG).show()
                    }.addOnFailureListener {
                        Toast.makeText(this,"An error occurred while updating the table status.",Toast.LENGTH_LONG).show()
                    }
                }
                else{
                    Toast.makeText(this,"You are not currently seated at any table.",Toast.LENGTH_LONG).show()
                }
            }.addOnFailureListener {
                Toast.makeText(this,"An error occurred while retrieving user information.",Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(this,"The user is not logged in.",Toast.LENGTH_LONG).show()
        }
    }

    fun sikayet(view: View){
        val intent = Intent(this, SikayetActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.animate_fade_enter,R.anim.animate_fade_exit)
    }

    fun cikisyap(view: View){
        auth.signOut()
        val intent = Intent(this,Login::class.java)
        startActivity(intent)
        finishAffinity()
    }

}