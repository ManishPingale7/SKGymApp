package com.example.skgym.mvvm.repository

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.skgym.Interfaces.BranchInterface
import com.example.skgym.Interfaces.DataAdded
import com.example.skgym.Interfaces.IsMemberCallBack
import com.example.skgym.activities.GetBranch
import com.example.skgym.activities.GetUserData
import com.example.skgym.activities.MainActivity
import com.example.skgym.activities.ViewPlan
import com.example.skgym.auth.HomeAuth
import com.example.skgym.data.Member
import com.example.skgym.data.Plan
import com.example.skgym.data.Product
import com.example.skgym.utils.Constants
import com.example.skgym.utils.Constants.BRANCHES
import com.example.skgym.utils.Constants.ISMEMBER
import com.example.skgym.utils.Constants.PLANS
import com.example.skgym.utils.Constants.PRODUCTS
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

abstract class BaseRepository(private var contextBase: Context) {
    var fDatabase = FirebaseDatabase.getInstance()
    val branchesList = MutableLiveData<ArrayList<String>>()
    private val plansRef = fDatabase.getReference(PLANS)
    var result = ""
    private var mAuthBase = FirebaseAuth.getInstance()
    private val userId = mAuthBase.uid.toString()

    private val isDataTaken: SharedPreferences =
        contextBase.getSharedPreferences("isDataTaken", Context.MODE_PRIVATE)
    val dataEdit = isDataTaken.edit()
    fun signOut() {
        mAuthBase.signOut()
    }

    fun sendUserToMainActivity() {
        Intent(contextBase, MainActivity::class.java).also {
            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            contextBase.startActivity(it)
        }
    }

    fun fetchBranchNames(branchInterface: BranchInterface): MutableLiveData<ArrayList<String>> {
        val list = ArrayList<String>()
        val branchesNameRef = fDatabase.getReference(Constants.BRANCHES_SPINNER)
        branchesNameRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dataSnapshot: DataSnapshot in snapshot.children) {
                    list.add(dataSnapshot.value.toString())
                }
                branchesList.value = list
                branchInterface.getBranch(list)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(TAG, "onCancelled: $error")
            }

        })
        return branchesList
    }

    fun fetchAllPlans(): MutableLiveData<ArrayList<Plan>> {
        val plans: MutableLiveData<ArrayList<Plan>> = MutableLiveData<ArrayList<Plan>>()
        val tempList = ArrayList<Plan>(10)
        tempList.clear()
        plansRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                plans.value?.clear()
                dataSnapshot.children.forEach {
                    Log.d(TAG, "onDataChange: $it")
                    tempList.add(it.getValue(Plan::class.java)!!)
                    Log.d("TAG", "onDataChange: $tempList")
                }
                plans.value = tempList
                Log.d("TAG", "onDataChange:${plans.value} ")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("HENLO", "onCancelled: $error")
            }
        })
        return plans
    }


    fun checkUserIsMember(branch: String, callback: IsMemberCallBack): String {
        val fDatabase = FirebaseDatabase.getInstance()
        val memberRef = fDatabase.getReference(BRANCHES)
        val userId = mAuthBase.uid

        CoroutineScope(IO).launch {
            Log.d(TAG, "checkUserIsMember: First Check")
            memberRef.child(branch).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.hasChild(userId.toString())) {
                        val member = snapshot.child(userId.toString())
                            .child(ISMEMBER).value.toString()
                        result = if (member == "true") {
                            Log.d(TAG, "checkUserIsMember: Checked True")
                            "true"
                        } else {
                            "false"
                        }
                    }

                    callback.onCallback(result)

                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d(TAG, "onCancelled: ${error.message}")
                }
            })
        }

        Log.d(TAG, "checkUserIsMember: Then Result")
        Log.d(TAG, "checkUserIsMember: result $result")
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


    fun uploadUserdata(memberThis: Member, dataAdded: DataAdded) {
        fDatabase.reference.child(BRANCHES).child(memberThis.branch).child(userId)
            .setValue(memberThis).addOnCompleteListener {
                if (it.isSuccessful) {
                    dataAdded.dataAdded(true)
                    Toast.makeText(contextBase, "Done", Toast.LENGTH_SHORT).show()
                } else if (it.isCanceled){
                    Toast.makeText(contextBase, "Error occurred", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "uploadUserdata: ${it.exception}")
                }
                else if (!it.isSuccessful){
                    Toast.makeText(contextBase, "Error occurred", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "uploadUserdata: ${it.exception}")
                }

            }
    }


    fun doesUserAndBranchExists(branch: String) {
        if (mAuthBase.currentUser != null) {
            fDatabase.reference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.hasChild(BRANCHES)) {
                        Log.d(TAG, "onDataChangeCheck: Branches Node Present")
                        if (snapshot.child(BRANCHES).hasChild(branch)) {
                            Log.d(TAG, "onDataChangeCheck: Branch Present")
                            Log.d(TAG, "onDataChangeCheck: User ID Check remaning = ${mAuthBase.currentUser!!.uid}")
                            if (snapshot.child(BRANCHES).child(branch)
                                    .hasChild(mAuthBase.currentUser!!.uid)
                            ) {
                                result = "dataPresent"
                                dataEdit.putBoolean("isDataTaken", true)
                                dataEdit.apply()
                                Log.d(TAG, "onDataChangeCheck: Result upper is $result")
                                Log.d(TAG, "onDataChangeCheck: User Present")
                            } else {
                                sendUserToDataActivity()
                            }
                        } else {
                            sendUserToDataActivity()
                        }
                    } else {
                        sendUserToDataActivity()
                    }
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
        }
    }

    fun sendUsertogetBranchActivity() {
        Intent(contextBase, GetBranch::class.java).also {
            contextBase.startActivity(it)
        }
    }

    fun forgotPassword(email: String) {
        if (email.isNotEmpty()) {
            mAuthBase.sendPasswordResetEmail(email).addOnCompleteListener {
                Toast.makeText(
                    contextBase,
                    "Reset link has been sent to Email",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
    }

    fun loadAllProducts(name: String): MutableLiveData<ArrayList<Product>> {
        val products: MutableLiveData<ArrayList<Product>> =
            MutableLiveData<ArrayList<Product>>()
        val tempList = ArrayList<Product>(50)

        val ref = fDatabase.reference.child(PRODUCTS).child(name)
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                products.value?.clear()
                Log.d(TAG, "onDataChange: $snapshot")
                snapshot.children.forEach {
                    tempList.add(it.getValue(Product::class.java)!!)
                }
                products.value = tempList
            }

            override fun onCancelled(error: DatabaseError) {
//                TODO("SOLVE EVERY ERROR SITUATION IN THE APP")
            }
        })
        return products
    }
}
