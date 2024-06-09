package com.example.randevusistemi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import java.util.Date

class SikayetActivity : AppCompatActivity() {

    private lateinit var etComplaint: EditText
    private lateinit var swAnonymous: Switch
    private lateinit var btnSendComplaint: Button
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sikayet)

        etComplaint = findViewById(R.id.etComplaint)
        swAnonymous = findViewById(R.id.swAnonymous)
        btnSendComplaint = findViewById(R.id.btnSendComplaint)

        firestore = Firebase.firestore
        auth = Firebase.auth

        btnSendComplaint.setOnClickListener {
            sendComplaint()
        }
    }

    private fun sendComplaint() {
        val complaintText = etComplaint.text.toString().trim()
        if (complaintText.isEmpty()) {
            Toast.makeText(this, "Please write your complaint", Toast.LENGTH_SHORT).show()
            return
        }

        val complaintsCollection = firestore.collection("complaints")
        val complaintData = mutableMapOf<String, Any>(
            "complaint" to complaintText,
            "timestamp" to Date()
        )

        if (swAnonymous.isChecked) {
            complaintData["email"] = "Anonymous"
        } else {
            complaintData["email"] = auth.currentUser?.email.toString()
        }

        complaintsCollection.add(complaintData)
            .addOnSuccessListener {
                Toast.makeText(this, "Your complaint has been submitted", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "An error occurred while submitting the complaint: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

}