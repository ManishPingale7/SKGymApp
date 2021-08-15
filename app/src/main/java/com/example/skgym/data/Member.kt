package com.example.skgym.data

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Member(
    var branch:String="",
    var imgUrl:Uri?=null,
    var firstname: String="empty",
    var middleName: String="empty",
    var lastname: String="empty",
    var bloodGroup:String="empty",
    var address: String="empty",
    var dob: Date=Date(1,1,1),
    var gender: String="empty",
    var guardian:String="None",
    var medicalDocuments:Uri?=null,
    var plan:String="",
    var isMember:Boolean=false
): Parcelable
