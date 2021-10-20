package com.example.skgym.fragments.mainNav

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.example.skgym.R
import com.example.skgym.auth.HomeAuth
import com.example.skgym.databinding.FragmentSettingsBinding
import com.example.skgym.di.component.DaggerFactoryComponent
import com.example.skgym.di.modules.FactoryModule
import com.example.skgym.di.modules.RepositoryModule
import com.example.skgym.mvvm.repository.MainRepository
import com.example.skgym.mvvm.viewmodles.MainViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class Settings : Fragment() {
    private lateinit var viewModel: MainViewModel
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private var currentUser: FirebaseUser? = null
    var mAuth = FirebaseAuth.getInstance()
    var branch = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        init()
        val isBranchTaken: SharedPreferences =
            requireActivity().getSharedPreferences("isBranchTaken", Context.MODE_PRIVATE)
        val branchTakenEdit = isBranchTaken.edit()
        val userBranch: SharedPreferences =
            requireActivity().getSharedPreferences("userBranch", Context.MODE_PRIVATE)
        val branchEdit = userBranch.edit()
        val isUserMemberChecked: SharedPreferences =
            requireActivity().getSharedPreferences("isUserMemberChecked", Context.MODE_PRIVATE)
        val isUserMemberCheckedEdit = isUserMemberChecked.edit()
        val isDataTaken: SharedPreferences =
            requireActivity().getSharedPreferences("isDataTaken", Context.MODE_PRIVATE)
        val isDatatakenEdit = isDataTaken.edit()
        branch = userBranch.getString("userBranch", "").toString()
        binding.currentBranchSettings.text = branch


        binding.ordersHistory.setOnClickListener {

        }


        binding.SignOutSettings.setOnClickListener {
            branchTakenEdit.putBoolean("isBranchTaken", false)
            branchTakenEdit.apply()
            branchEdit.putString("userBranch", "")
            branchEdit.apply()
            isDatatakenEdit.putBoolean("isDataTaken", false)
            isDatatakenEdit.apply()
            isUserMemberCheckedEdit.putBoolean("isUserMemberChecked", false)
            isUserMemberCheckedEdit.apply()
            mAuth.signOut()
            val intent = Intent(
                requireContext(), HomeAuth::class.java
            )
            startActivity(intent)
            requireActivity().finish()
        }
        binding.refreshMemberStatus.setOnClickListener {
            Toast.makeText(requireContext(), "Checking", Toast.LENGTH_SHORT).show()
            branchTakenEdit.putBoolean("isBranchTaken", false)
            branchTakenEdit.apply()
            branchEdit.putString("userBranch", "")
            branchEdit.apply()
            isUserMemberCheckedEdit.putBoolean("isUserMemberChecked", false)
            isUserMemberCheckedEdit.apply()
            viewModel.sendUserToMainActivity()
        }
        binding.viewAllPlans.setOnClickListener {
            viewModel.sendUserToViewPlanActivity()
        }

        binding.currentPlanSettings.setOnClickListener {



        }

        binding.viewProductsSettings.setOnClickListener {
            binding.root.findNavController().navigate(
                R.id.action_nav_settings_to_products,
                null, // Bundle of args
                null
            )
        }
        return binding.root
    }

    private fun init() {
        val component: DaggerFactoryComponent = DaggerFactoryComponent.builder()
            .repositoryModule(RepositoryModule(requireContext()))
            .factoryModule(FactoryModule(MainRepository(requireContext())))
            .build() as DaggerFactoryComponent
        viewModel =
            ViewModelProviders.of(this@Settings, component.getFactory())
                .get(MainViewModel::class.java)
        mAuth = FirebaseAuth.getInstance()
        currentUser = mAuth.currentUser
    }


}