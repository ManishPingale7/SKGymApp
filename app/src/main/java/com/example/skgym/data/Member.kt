package com.example.skgym.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Member(
    var branch:String="",
    var name: String="empty",
    var gender: String="empty",
    var planKey:String="",
    var member: Boolean = false
): Parcelable
