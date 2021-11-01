package com.example.skgym.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Cart(
    val product: String,
    val quantity: Int,
    val paymentDone: Boolean = false,
    val purchasedAt: String = " ",
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)

