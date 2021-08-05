package com.example.skgym.mvvm.repository

import android.content.Context
import com.google.firebase.auth.FirebaseAuth

abstract class BaseRepository(private var contextBase: Context) {


    private var mAuthBase = FirebaseAuth.getInstance()
    var curUser=mAuthBase.currentUser

    fun signOut() {
        mAuthBase.signOut()
    }


}