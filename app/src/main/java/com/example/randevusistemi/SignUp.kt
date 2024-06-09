package com.example.randevusistemi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.randevusistemi.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignUp : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = FirebaseAuth.getInstance() // Doğru başlatma yöntemi
        firestore = FirebaseFirestore.getInstance() // Doğru başlatma yöntemi
    }

    fun signUp(view: View){
        val username = binding.userNameText.text.toString().trim()
        val email = binding.emailText.text.toString().trim()
        val password = binding.passwordText.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Email and Password cannot be empty", Toast.LENGTH_LONG).show()
        } else {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val firebaseUser = task.result?.user  // Kullanıcı nesnesine erişim
                        if (firebaseUser != null) {
                            val users = hashMapOf(
                                "name" to username,
                                "email" to email,
                                "password" to password
                            )
                            // Kullanıcının UID'sini kullanarak Firestore belgesi oluşturma
                            firestore.collection("users").document(firebaseUser.uid).set(users)
                                .addOnSuccessListener {
                                    Toast.makeText(this, "User registered successfully", Toast.LENGTH_SHORT).show()
                                    val intent = Intent(this@SignUp, Login::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(this, "Failed to register user: ${it.localizedMessage}", Toast.LENGTH_LONG).show()
                                }
                        }
                    } else {
                        Toast.makeText(this, "Registration failed: ${task.exception?.localizedMessage}", Toast.LENGTH_LONG).show()
                    }
                }
        }
    }
}
