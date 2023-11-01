package com.example.foodhub

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        //for the splash screen
        Handler().postDelayed({
                              startActivity(Intent(this@SplashActivity,StartActivity::class.java))
            finish()
        },2000)
    }
}