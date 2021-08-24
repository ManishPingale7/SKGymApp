package com.example.skgym.mvvm.repository

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.skgym.activities.GetUserData
import com.example.skgym.activities.MainActivity
import com.example.skgym.activities.ViewPlan
import com.example.skgym.auth.HomeAuth
import com.example.skgym.data.Member
import com.example.skgym.utils.Constants
import com.example.skgym.utils.Constants.ISMEMBER
import com.example.skgym.utils.Constants.USERS
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

abstract class BaseRepository(private var contextBase: Context) {
    var fDatabase = FirebaseDatabase.getInstance()
    val branchesList = MutableLiveData<java.util.ArrayList<String>>()

    private var mAuthBase = FirebaseAuth.getInstance()
    private val userId = mAuthBase.uid.toString()


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
                Log.d(TAG, "onCancelled: $error")
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


    fun sendUserToViewPlanActivity() {
        Intent(contextBase, ViewPlan::class.java).also {
            contextBase.startActivity(it)
        }
    }

    fun sendUserToDataActivity() {
        Intent(contextBase, GetUserData::class.java).also {
            contextBase.startActivity(it)
        }
    }


    fun doesUserExists(branch: String) {
        fDatabase = FirebaseDatabase.getInstance()
        val memberRef = fDatabase.getReference(branch)
        val userId = mAuthBase.uid
        var result = ""

        memberRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d(TAG, "onDataChange: $userId")
                val user = snapshot.hasChild(userId.toString())
                Log.d(TAG, "onDataChange: OnDataChange user is $user")
                if (user) {
                    result = "dataPresent"
                    Log.d(TAG, "onDataChange: Result upper is $result")
                } else {
                    sendUserToDataActivity()
                    Toast.makeText(contextBase, "No User Data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(TAG, "onCancelled: ${error.message}")
            }
        })
    }

    fun uploadUserdata(memberThis: Member) {
        fDatabase.reference.child(memberThis.branch).child(userId).setValue(memberThis)
    }

    fun doesBranchExists(branch: String) {
        if (mAuthBase.currentUser!=null){
            fDatabase.reference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.hasChild(branch))
                        doesUserExists(branch)
                    else
                        sendUserToDataActivity()
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d(TAG, "onCancelled: ${error.message}")
                }

            })
        }
    }

    fun sendUserToHomeAuth() {
        Intent(contextBase, HomeAuth::class.java).also {
            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            contextBase.startActivity(it)
        }    }


}