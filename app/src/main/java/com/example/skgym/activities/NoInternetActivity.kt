package com.example.skgym.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.skgym.R
import com.example.skgym.databinding.ActivityNoInternetBinding
import com.example.skgym.databinding.ActivityViewProductsInCategoryBinding

class NoInternetActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNoInternetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoInternetBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}