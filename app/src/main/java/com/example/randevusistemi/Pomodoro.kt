package com.example.randevusistemi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import com.example.randevusistemi.databinding.ActivityPomodoroBinding

class Pomodoro : AppCompatActivity() {

    private lateinit var binding: ActivityPomodoroBinding
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var timer: CountDownTimer
    private var isWorkTime = true
    private var isRunning = false
    private var workTimeInMillis: Long = 1500000L // Default 25 minutes
    private var breakTimeInMillis: Long = 300000L // Default 5 minutes
    private var timeLeftInMillis: Long = workTimeInMillis
    private var totalRounds = 4
    private var currentRound = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPomodoroBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.npWorkTime.maxValue = 60
        binding.npWorkTime.minValue = 1
        binding.npBreakTime.maxValue = 30
        binding.npBreakTime.minValue = 1
        binding.npRounds.maxValue = 10
        binding.npRounds.minValue = 1

        setupNumberPickers()

        binding.btnStart.setOnClickListener {
            if (isRunning) {
                pauseTimer()
            } else {
                startTimer()
            }
        }

        binding.btnReset.setOnClickListener {
            resetTimer()
        }

        updateCountDownText(timeLeftInMillis)

        toggle = ActionBarDrawerToggle(this,binding.homeDrawerLayout,R.string.navigation_drawer_open,R.string.navigation_drawer_close)

        binding.bottomNavigation.background = null

        binding.bottomNavigation.selectedItemId = R.id.bottom_pomodoro

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
                    /*val intent = Intent(this, Pomodoro::class.java)
                    startActivity(intent)
                    finish()*/
                    Toast.makeText(this,"You are already on this page!!", Toast.LENGTH_LONG).show()}
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

    private fun setupNumberPickers() {
        binding.npWorkTime.minValue = 1
        binding.npWorkTime.maxValue = 60
        binding.npWorkTime.value = 25

        binding.npBreakTime.minValue = 1
        binding.npBreakTime.maxValue = 30
        binding.npBreakTime.value = 5

        binding.npRounds.minValue = 1
        binding.npRounds.maxValue = 10
        binding.npRounds.value = 4

        binding.npWorkTime.setOnValueChangedListener { _, _, newVal ->
            if (isWorkTime) {
                workTimeInMillis = newVal * 60000L
                timeLeftInMillis = workTimeInMillis
                updateCountDownText(timeLeftInMillis)
            }
        }

        binding.npBreakTime.setOnValueChangedListener { _, _, newVal ->
            if (!isWorkTime) {
                breakTimeInMillis = newVal * 60000L
                timeLeftInMillis = breakTimeInMillis
                updateCountDownText(timeLeftInMillis)
            }
        }

        binding.npRounds.setOnValueChangedListener { _, _, newVal ->
            totalRounds = newVal
        }
    }

    private fun startTimer() {
        isRunning = true
        binding.btnStart.text = "Pause"
        timer = object : CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                updateCountDownText(millisUntilFinished)
            }

            override fun onFinish() {
                if (isWorkTime) {
                    isWorkTime = false
                    resetBreakTime()
                    startBreakTimer()
                } else {
                    isWorkTime = true
                    currentRound++
                    if (currentRound <= totalRounds) {
                        resetWorkTime()
                        startWorkTimer()
                    } else {
                        isRunning = false
                        binding.btnStart.text = "Start"
                        Toast.makeText(this@Pomodoro, "Pomodoro Completed!", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }.start()
    }

    private fun startWorkTimer() {
        binding.tvStatus.text = "Work"
        timeLeftInMillis = workTimeInMillis
        startTimer()
    }

    private fun startBreakTimer() {
        binding.tvStatus.text = "Break"
        timeLeftInMillis = breakTimeInMillis
        startTimer()
    }

    private fun pauseTimer() {
        timer.cancel()
        isRunning = false
        binding.btnStart.text = "Start"
    }

    private fun resetTimer() {
        if (isRunning) {
            timer.cancel()
        }
        isRunning = false
        isWorkTime = true
        resetWorkTime()
        resetBreakTime()
        currentRound = 1
        updateCountDownText(workTimeInMillis)
        binding.btnStart.text = "Start"
        binding.tvStatus.text = "Work"
    }

    private fun resetWorkTime() {
        workTimeInMillis = binding.npWorkTime.value * 60000L
        if (isWorkTime) {
            timeLeftInMillis = workTimeInMillis
        }
    }

    private fun resetBreakTime() {
        breakTimeInMillis = binding.npBreakTime.value * 60000L
        if (!isWorkTime) {
            timeLeftInMillis = breakTimeInMillis
        }
    }

    private fun updateCountDownText(millisLeft: Long) {
        val minutes = (millisLeft / 1000) / 60
        val seconds = (millisLeft / 1000) % 60
        val timeFormatted = String.format("%02d:%02d", minutes, seconds)
        binding.tvTimer.text = timeFormatted
    }

}