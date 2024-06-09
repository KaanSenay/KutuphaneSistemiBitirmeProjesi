package com.example.randevusistemi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.example.randevusistemi.databinding.ActivityOdalarBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class OdalarActivity : AppCompatActivity(){

    private lateinit var binding: ActivityOdalarBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOdalarBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = Firebase.auth
        firestore = Firebase.firestore

        toggle = ActionBarDrawerToggle(this,binding.homeDrawerLayout,R.string.navigation_drawer_open,R.string.navigation_drawer_close)
        //binding.homeDrawerLayout.addDrawerListener(toggle)
        //toggle.syncState()
        //supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.bottomNavigation.background = null

        binding.bottomNavigation.selectedItemId = R.id.bottom_rooms

        binding.bottomNavigation.setOnItemSelectedListener{item ->
            when (item.itemId) {
                R.id.bottom_home -> {
                    val intent = Intent(this, Home::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.animate_swipe_right_enter,R.anim.animate_swipe_right_exit)
                    finish()}
                R.id.bottom_rooms -> {
                    /*val intent = Intent(this, OdalarActivity::class.java)
                    startActivity(intent)
                    finish()*/
                    Toast.makeText(this,"You are already on this page!!",Toast.LENGTH_LONG).show()}
                R.id.bottom_pomodoro -> {
                    val intent = Intent(this, Pomodoro::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.animate_swipe_left_enter,R.anim.animate_swipe_left_exit)
                    finish()}
                R.id.bottom_borrow -> {
                    val intent = Intent(this, Kitap::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.animate_swipe_left_enter,R.anim.animate_swipe_left_exit)
                    finish()}
                R.id.bottom_options -> {
                    val intent = Intent(this, Options::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.animate_swipe_left_enter,R.anim.animate_swipe_left_exit)
                    finish()}
            }
            true
        }


        handler = Handler(Looper.getMainLooper())
        runnable = object : Runnable {
            override fun run() {
                getCurrentUserTable()
                // 5 saniye sonra tekrar çalıştır
                handler.postDelayed(this, 5000)
            }
        }
        handler.post(runnable)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            return true
        } else {
            return super.onOptionsItemSelected(item)
        }
    }

    private fun getCurrentUserTable() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val userRef = firestore.collection("users").document(currentUser.uid)
            userRef.get().addOnSuccessListener { document ->
                if (document.exists()) {
                    val currentTable = document.getString("currentTable")
                    if (currentTable != null) {
                        binding.textViewMasa.text = "You are currently seated at table $currentTable"
                    } else {
                        binding.textViewMasa.text = "You are not currently seated at any table."
                    }
                } else {
                    binding.textViewMasa.text = "User information not found."
                }
            }.addOnFailureListener {
                binding.textViewMasa.text = "An error occurred while retrieving user information."
            }
        } else {
            binding.textViewMasa.text = "The user is not logged in."
        }
    }

    fun oda1(view: View){
        val intent = Intent(this, MainApp::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.animate_fade_enter,R.anim.animate_fade_exit)
    }

    fun oda2(view: View){
        val intent = Intent(this, MainApp2::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.animate_fade_enter,R.anim.animate_fade_exit)
    }

    fun oda3(view: View){
        val intent = Intent(this, MainApp3::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.animate_fade_enter,R.anim.animate_fade_exit)
    }

    fun oda4(view: View){
        val intent = Intent(this, MainApp4::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.animate_fade_enter,R.anim.animate_fade_exit)
    }

    fun oda5(view: View){
        val intent = Intent(this, MainApp5::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.animate_fade_enter,R.anim.animate_fade_exit)
    }

    fun oda6(view: View){
        val intent = Intent(this, MainApp6::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.animate_fade_enter,R.anim.animate_fade_exit)
    }

}