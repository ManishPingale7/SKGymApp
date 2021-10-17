package com.example.skgym.data

import android.os.Parcelable

@Parcelize
data class Member(
    var branch:String="",
    var name: String="empty",
    var gender: String="empty",
    var planKey:String="",
    var member: Boolean = false
): Parcelable
