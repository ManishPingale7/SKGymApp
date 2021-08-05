package com.example.skgym.mvvm.repository

import android.content.Context
import android.content.Intent
import com.example.skgym.activities.MainActivity
import com.google.firebase.auth.FirebaseAuth

abstract class BaseRepository(private var contextBase: Context) {


    private var mAuthBase = FirebaseAuth.getInstance()
    var curUser=mAuthBase.currentUser

    fun signOut() {
        mAuthBase.signOut()
    }

    fun sendUserToMainActivity() {
        Intent(contextBase, MainActivity::class.java).also {
            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            contextBase.startActivity(it)
        }
    }


}