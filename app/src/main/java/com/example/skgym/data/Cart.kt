package com.example.skgym.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


@Entity
@Parcelize
data class Cart(
    val product: String,
    val quantity: Int,
    val paymentDone: Boolean = false,
    @PrimaryKey(autoGenerate = true) val productId: Int = 0
) : Parcelable