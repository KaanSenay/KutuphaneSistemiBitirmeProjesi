package com.example.randevusistemi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class AdminActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)
    }

    fun bookreturn(view: View){
        val intent = Intent(this, AdminBookActivity::class.java)
        startActivity(intent)
    }

    fun cikisyap(view: View){
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        finishAffinity()
    }

    fun tableuser(view: View){
        val intent = Intent(this, TablesAndUsers::class.java)
        startActivity(intent)
    }

    fun delete(view: View){
        val intent = Intent(this, UserDelete::class.java)
        startActivity(intent)
    }
}