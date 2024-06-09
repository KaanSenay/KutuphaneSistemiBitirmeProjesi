package com.example.randevusistemi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import com.example.randevusistemi.databinding.ActivityKitapBinding

class Kitap : AppCompatActivity() {
    private lateinit var binding: ActivityKitapBinding
    private lateinit var toggle: ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKitapBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        toggle = ActionBarDrawerToggle(this,binding.homeDrawerLayout,R.string.navigation_drawer_open,R.string.navigation_drawer_close)

        binding.bottomNavigation.background = null

        binding.bottomNavigation.selectedItemId = R.id.bottom_borrow

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
                    /*val intent = Intent(this, Kitap::class.java)
                    startActivity(intent)
                    finish()*/
                    Toast.makeText(this,"You are already on this page!!", Toast.LENGTH_LONG).show()}
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

    fun kitapoduncal(view: View){
        val intent = Intent(this, BookListActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.animate_fade_enter,R.anim.animate_fade_exit)
    }

    fun kitapiade(view: View){
        val intent = Intent(this, ReturnBooksActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.animate_fade_enter,R.anim.animate_fade_exit)
    }

}