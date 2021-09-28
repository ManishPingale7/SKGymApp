package com.example.skgym.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Member(
    var branch:String="",
    var imgUrl:String?="null",
    var name: String="empty",
    var bloodGroup:String="empty",
    var address: String="empty",
    var dob: String="",
    var gender: String="empty",
    var guardian:String="None",
    var medicalDocuments:String?="null",
    var plan:String="",
    var member: Boolean = false
): Parcelable
