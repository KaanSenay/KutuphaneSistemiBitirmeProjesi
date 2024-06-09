package com.example.randevusistemi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import com.example.randevusistemi.databinding.ActivityHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Home : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = Firebase.auth
        firestore = Firebase.firestore
        firestore.collection("users").get().addOnSuccessListener { result ->
            for (document in result){
                if (auth.currentUser?.email == document.getString("email")){
                    binding.hosgeldinyazi.text = "Welcome " + document.getString("name")
                }
            }
        }.addOnFailureListener(){
            Toast.makeText(this,"ERROR!!!",Toast.LENGTH_LONG).show()
        }

        toggle = ActionBarDrawerToggle(this,binding.homeDrawerLayout,R.string.navigation_drawer_open,R.string.navigation_drawer_close)

        binding.bottomNavigation.background = null

        binding.bottomNavigation.selectedItemId = R.id.bottom_home

        binding.bottomNavigation.setOnItemSelectedListener{item ->
            when (item.itemId) {
                R.id.bottom_home -> {
                    /*val intent = Intent(this, Home::class.java)
                    startActivity(intent)*/
                    Toast.makeText(this,"You are already on this page!!",Toast.LENGTH_LONG).show()}
                R.id.bottom_rooms -> {
                    val intent = Intent(this, OdalarActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.animate_swipe_left_enter,R.anim.animate_swipe_left_exit)
                    finish()}
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

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            return true
        } else {
            return super.onOptionsItemSelected(item)
        }
    }

}