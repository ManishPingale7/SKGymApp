package com.example.skgym.data

import android.net.Uri
import java.util.*

data class Member(
    var imgUrl:Uri?=null,
    var firstname: String="empty",
    var middleName: String="empty",
    var lastname: String="empty",
    var bloodGroup:String="empty",
    var address: String="empty",
    var dob: Date=Date(1,1,1),
    var gender: String="empty",
    var guardian:String="None",
    var medicalDocuments:Uri?=null
)
