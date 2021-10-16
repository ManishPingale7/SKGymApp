package com.example.skgym.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


@Entity
@Parcelize
data class Cart(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val product: Product,
    val quantity: Int,
    val paymentDone: Boolean = false
)