package com.sofit.test

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import com.sofit.test.activities.LoginActivity
import com.sofit.test.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        callMainScreen()
    }
    private fun callMainScreen(){
        object : CountDownTimer(3000, 3000) {
            override fun onTick(millisUntilFinished: Long) {} //end of onTick
            override fun onFinish() {
                val callHomeIntent = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(callHomeIntent)
                finish()
            }
        }.start()
    }

}