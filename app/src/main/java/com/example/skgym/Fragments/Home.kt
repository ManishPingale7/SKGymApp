package com.example.skgym.Fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Context.MODE_PRIVATE
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.lifecycle.ViewModelProviders
import com.example.skgym.R
import com.example.skgym.activities.GetUserData
import com.example.skgym.databinding.FragmentHomeBinding
import com.example.skgym.di.component.DaggerFactoryComponent
import com.example.skgym.di.modules.FactoryModule
import com.example.skgym.di.modules.RepositoryModule
import com.example.skgym.mvvm.repository.MainRepository
import com.example.skgym.mvvm.viewmodles.MainViewModel
import com.example.skgym.utils.Constants.BRANCHES_SPINNER
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*


class Home : Fragment() {
    private lateinit var viewModel: MainViewModel
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    lateinit var currentUser: FirebaseUser
    var mAuth = FirebaseAuth.getInstance()
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
            val builder = AlertDialog.Builder(requireContext())
            val mView = layoutInflater.inflate(R.layout.dialogspinner, null)
            builder.setTitle("Select Your Branch")
            val spinner = mView.findViewById<Spinner>(R.id.spinnerBranches)



            branchesNameRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    branchesList.clear()
                    for (dataSnapshot: DataSnapshot in snapshot.children) {
                        branchesList.add(dataSnapshot.value.toString())
                        Log.d(TAG, "onDataChange: ${dataSnapshot.value.toString()}")
                        Log.d(TAG, "onDataChange: ${branchesList.size}")
                    }
                    val list = branchesList.toArray()
                    Log.d(TAG, "onCreateView: size ${list.size}")
                    val adapter = ArrayAdapter(
                        requireContext(), android.R.layout.simple_spinner_item,
                        list
                    )
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinner.adapter = adapter
                    adapter.notifyDataSetChanged()
                    builder.setPositiveButton(
                        "ok"
                    ) { dialog, which ->

                    }

                    builder.setNegativeButton(
                        "Cancel"
                    ) { dialog, which ->

                    }
                    builder.setView(mView)
                    builder.show()
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d(TAG, "onCancelled: $error")
                }

            })

            for (i in 0 until branchesList.size) {
                Log.d(TAG, "onCreateView: Array ${branchesList[i]}")
            }

        }



        return binding.root
    }


    private fun init() {
        val component: DaggerFactoryComponent = DaggerFactoryComponent.builder()
            .repositoryModule(RepositoryModule(requireContext()))
            .factoryModule(FactoryModule(MainRepository(requireContext())))
            .build() as DaggerFactoryComponent
        viewModel =
            ViewModelProviders.of(this, component.getFactory()).get(MainViewModel::class.java)
        mAuth = FirebaseAuth.getInstance()

    }


}