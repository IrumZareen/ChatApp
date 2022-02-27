package com.sofit.test.activities

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sofit.test.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val firebaseAuth = Firebase.auth
    val currentUser = firebaseAuth.currentUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        callMainScreen()
    }

    private fun callMainScreen() {
        object : CountDownTimer(3000, 3000) {
            override fun onTick(millisUntilFinished: Long) {} //end of onTick
            override fun onFinish() {
                if (currentUser != null) {  // Check if user is signed in (non-null) and update UI accordingly.
                    startHomeActivity();
                } else {
                    val intent = Intent(this@MainActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }.start()
    }


    private fun startHomeActivity() {
        val intent = Intent(this@MainActivity, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

}