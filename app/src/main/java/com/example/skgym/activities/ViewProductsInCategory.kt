package com.example.skgym.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import com.example.skgym.databinding.ActivityViewProductsInCategoryBinding

class ViewProductsInCategory : AppCompatActivity() {
    private lateinit var binding: ActivityViewProductsInCategoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityViewProductsInCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }


}