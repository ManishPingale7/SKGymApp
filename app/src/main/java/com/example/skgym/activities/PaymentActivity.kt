package com.example.skgym.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.skgym.R
import com.example.skgym.data.Plan

class PaymentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        if (intent.hasExtra("Plan"))
            Toast.makeText(
                applicationContext,
                "Plan: " + intent.getParcelableExtra<Plan>("Plan"),
                Toast.LENGTH_SHORT
            ).show()

    }
}