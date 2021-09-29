package com.example.skgym.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Plan(
    val name: String,
    val desc: String,
    val timeNumber: String,
    val fees: String,
    val pt:Boolean?
) : Parcelable {
    constructor() : this("", "", "",  "",null)
}