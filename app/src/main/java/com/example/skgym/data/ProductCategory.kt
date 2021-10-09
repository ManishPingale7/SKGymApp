package com.example.skgym.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductCategory(
    val name: String,
    val image: String,
) : Parcelable {
    constructor() : this("", "")
}