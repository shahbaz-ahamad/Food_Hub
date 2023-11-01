package com.example.foodhub

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.foodhub.databinding.ActivityMainBinding
import com.example.foodhub.fragment.HomeFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val binding : ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupNavHostFragmentWithBottomNavView()
    }

    private fun setupNavHostFragmentWithBottomNavView() {
        val navHostFragment =supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController :NavController =navHostFragment.navController

        //associate it with bottom navigationview
        val bottomNavigationView =binding.bottomNavigationView
        bottomNavigationView.setupWithNavController(navController)
    }


    override fun onBackPressed() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        // Get the current destination of the NavController
        val currentDestinationId = navController.currentDestination?.id

        if (currentDestinationId == R.id.homeFragment) {
            // If the current fragment is the home fragment, show a Snackbar to confirm exit
            val rootView = findViewById<View>(android.R.id.content)

            Snackbar.make(rootView, "Do you want to exit the app?", Snackbar.LENGTH_LONG)
                .setAction("Exit") {
                    finishAffinity() // Exit the app
                }
                .show()
        } else {
            // If the current fragment is not the home fragment, allow the default back behavior
            super.onBackPressed()
        }
    }

}