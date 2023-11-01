package com.example.foodhub

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.foodhub.databinding.ActivityStartBinding

class StartActivity : AppCompatActivity() {

    private val binding : ActivityStartBinding by lazy {
        ActivityStartBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        checkNextButtonIsClickedOrNot()
    }

    private fun checkNextButtonIsClickedOrNot() {
        val sharedPreferences = getSharedPreferences("YourPreferencesName", MODE_PRIVATE)
        val hasUserGoneThroughSetup = sharedPreferences.getBoolean("hasUserGoneThroughSetup", false)
        if (hasUserGoneThroughSetup) {
            // If the button has been clicked, navigate to LoginActivity
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        } else {
            binding.buttonNext.setOnClickListener {
                // When the button is clicked, mark it in SharedPreferences
                val editor = sharedPreferences.edit()
                editor.putBoolean("hasUserGoneThroughSetup", true)
                editor.apply()
                // Then, navigate to LoginActivity
                startActivity(Intent(this@StartActivity, LoginActivity::class.java))
                finish()
            }
        }
    }
}