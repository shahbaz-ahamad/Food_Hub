package com.example.foodhub.helper

import android.view.View
import androidx.fragment.app.Fragment
import com.example.foodhub.MainActivity
import com.example.foodhub.R
import com.google.android.material.bottomnavigation.BottomNavigationView

fun Fragment.showBottomNavigation(){

    val bottomNavigation= (activity as MainActivity).findViewById<BottomNavigationView>(R.id.bottomNavigationView)
    bottomNavigation.visibility=View.VISIBLE
}

fun Fragment.HideBottomNavigation(){
    val bottomNavigation = (activity as MainActivity).findViewById<BottomNavigationView>(R.id.bottomNavigationView)
    bottomNavigation.visibility=View.INVISIBLE
}