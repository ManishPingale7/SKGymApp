package com.example.skgym.mvvm.repository

import android.content.Context
import com.google.firebase.auth.FirebaseAuth

class MainRepository(private var contextMain: Context) : BaseRepository(contextMain) {

    private var mAuthMain = FirebaseAuth.getInstance()
    var curUserMain=mAuthMain.currentUser




}