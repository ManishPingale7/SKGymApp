package com.example.skgym.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.DateFormat

@Entity
data class Cart(
    val product: String,
    val quantity: Int,
    val paymentDone: Boolean = false,
    val purchasedAt: String = DateFormat.getDateInstance().format(System.currentTimeMillis()),
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)

