package com.example.randevusistemi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.randevusistemi.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Login : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = Firebase.auth

        val currentUser = auth.currentUser

        if (currentUser != null) {
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun login(view: View){
        val email = binding.editEmailText.text.toString()
        val password = binding.editPasswordText.text.toString()

        if (email.equals("") || password.equals("")) {
            Toast.makeText(this@Login,"Enter your Email and Password !!", Toast.LENGTH_LONG).show()
        } else if (email.equals("admin") || password.equals("admin")) {
            val intent = Intent(this@Login, AdminActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            auth.signInWithEmailAndPassword(email,password).addOnSuccessListener {
                val intent = Intent(this@Login, Home::class.java)
                startActivity(intent)
                finish()
            }.addOnFailureListener {
                Toast.makeText(this,it.localizedMessage, Toast.LENGTH_LONG).show()
            }

        }
    }

    fun signup(view: View){
        val intent = Intent(this@Login, SignUp::class.java)
        startActivity(intent)
        finish()
    }

}