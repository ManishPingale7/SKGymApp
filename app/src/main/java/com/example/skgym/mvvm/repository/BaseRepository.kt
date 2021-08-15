package com.example.skgym.mvvm.repository

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.skgym.activities.MainActivity
import com.example.skgym.utils.Constants
import com.example.skgym.utils.Constants.ISMEMBER
import com.example.skgym.utils.Constants.USERS
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

abstract class BaseRepository(private var contextBase: Context) {

    val branchesList = MutableLiveData<java.util.ArrayList<String>>()


    private var mAuthBase = FirebaseAuth.getInstance()
    var curUser = mAuthBase.currentUser

    fun signOut() {
        mAuthBase.signOut()
    }

    fun sendUserToMainActivity() {
        Intent(contextBase, MainActivity::class.java).also {
            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            contextBase.startActivity(it)
        }
    }

    fun fetchBranchNames(): MutableLiveData<ArrayList<String>> {
        val fDatabase = FirebaseDatabase.getInstance()
        val list = java.util.ArrayList<String>()
        val branchesNameRef = fDatabase.getReference(Constants.BRANCHES_SPINNER)
        branchesNameRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dataSnapshot: DataSnapshot in snapshot.children) {
                    list.add(dataSnapshot.value.toString())
                }
                branchesList.value = list
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(ContentValues.TAG, "onCancelled: $error")
            }

        })
        return branchesList
    }

    fun checkUserIsMember(branch: String): Boolean {
        val fDatabase = FirebaseDatabase.getInstance()
        val memberRef = fDatabase.getReference(branch)
        val userId = mAuthBase.uid
        var result = false
        memberRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.hasChild(USERS)) {
                    if (snapshot.child(USERS).hasChild(userId.toString())) {
                        if (snapshot.child(USERS).child(userId.toString())
                                .child(ISMEMBER).value == true
                        ) {
                            result = true
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(TAG, "onCancelled: ${error.message}")
            }
        })
        return result
    }


}