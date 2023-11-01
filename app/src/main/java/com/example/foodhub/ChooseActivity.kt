package com.example.foodhub

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.example.foodhub.databinding.ActivityChooseBinding

class ChooseActivity : AppCompatActivity() {
    private val binding : ActivityChooseBinding by lazy {
        ActivityChooseBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        //choose location
        setupChooseLocationDropdown()
    }

    private fun setupChooseLocationDropdown() {
        val location = arrayListOf<String>("Rajkot","Jamnagar","Ahmedabad")
        val adapter = ArrayAdapter(this@ChooseActivity,android.R.layout.simple_list_item_1,location)
        binding.ListOfLocation.setAdapter(adapter)
    }
}