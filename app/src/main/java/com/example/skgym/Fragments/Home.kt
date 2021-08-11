package com.example.skgym.Fragments

import android.content.ContentValues.TAG
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.skgym.R
import com.example.skgym.activities.GetUserData
import com.example.skgym.databinding.FragmentHomeBinding
import com.example.skgym.mvvm.viewmodles.MainViewModel
import com.example.skgym.utils.Constants.BRANCHES_SPINNER
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase


class Home : Fragment() {
    private lateinit var viewModel: MainViewModel
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    lateinit var currentUser: FirebaseUser
    var mAuth = FirebaseAuth.getInstance()
    var branchesListHash = HashMap<String, String>()
    var branchesList = ArrayList<String>()

    private val fDatabase = FirebaseDatabase.getInstance()
    var branchesNameRef = fDatabase.getReference(BRANCHES_SPINNER)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        init()

        binding.homedata.setOnClickListener {
            val intent = Intent(requireContext(), GetUserData::class.java)
            startActivity(intent)
        }

        //Dark Mode Settings
        val isBranchTaken: SharedPreferences =
            requireActivity().getSharedPreferences("isBranchTaken", MODE_PRIVATE)
        val editor = isBranchTaken.edit()

        val isTaken = isBranchTaken.getBoolean("isBranchTaken", false)

        if (isTaken) {
            Log.d(TAG, "onCreateView: Branch taken")
        } else {
            branchesNameRef.addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    branchesListHash[snapshot.key.toString()] = snapshot.value.toString()
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    branchesListHash[snapshot.key.toString()] = snapshot.value.toString()
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    Log.d(TAG, "onChildRemoved: remaining")
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    Log.d(TAG, "onChildRemoved: remaining")

                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d(TAG, "onChildRemoved: remaining")
                }

            })

            branchesList.addAll(branchesListHash.values)

            val adapter=ArrayAdapter<String>(requireContext(),android.R.layout.simple_dropdown_item_1line)
            adapter.addAll(branchesList)

            MaterialAlertDialogBuilder(requireContext())
                .setTitle(resources.getString(R.string.app_name))
                .setAdapter(adapter) { dialog, which ->
                    // Respond to item chosen
                }
                .show()
        }



        return binding.root
    }

    private fun init() {
        mAuth = FirebaseAuth.getInstance()

    }


}