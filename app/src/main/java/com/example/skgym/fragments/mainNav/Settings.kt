package com.example.skgym.fragments.mainNav

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.example.skgym.R
import com.example.skgym.activities.OrdersActivity
import com.example.skgym.activities.PlansHistory
import com.example.skgym.auth.HomeAuth
import com.example.skgym.data.Plan
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
        val isDataTakenEdit = isDataTaken.edit()
        val currentPlanPref: SharedPreferences =
            requireActivity().getSharedPreferences("currentPlan", Context.MODE_PRIVATE)
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_view_plan);
        dialog.window?.setBackgroundDrawable(
            getDrawable(
                requireContext(),
                R.drawable.custom_dialog_background
            )
        )
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        dialog.setCancelable(true)
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        val okay: Button = dialog.findViewById(R.id.btn_okay)
        val cancel: Button = dialog.findViewById(R.id.btn_cancel)
        val planName: TextView = dialog.findViewById(R.id.planNameDialog)
        okay.setOnClickListener {
            Toast.makeText(requireContext(), "Okay", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        cancel.setOnClickListener {
            Toast.makeText(requireContext(), "Cancel", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        branch = userBranch.getString("userBranch", "").toString()
        binding.currentBranchSettings.text = branch

        binding.viewPlansHis.setOnClickListener {
            startActivity(Intent(requireContext(), PlansHistory::class.java))
        }

        binding.ordersHistory.setOnClickListener {
            startActivity(Intent(requireContext(), OrdersActivity::class.java))
        }

        binding.SignOutSettings.setOnClickListener {
            branchTakenEdit.putBoolean("isBranchTaken", false)
            branchTakenEdit.apply()
            branchEdit.putString("userBranch", "")
            branchEdit.apply()
            isDataTakenEdit.putBoolean("isDataTaken", false)
            isDataTakenEdit.apply()
            isUserMemberCheckedEdit.putBoolean("isUserMemberChecked", false)
            isUserMemberCheckedEdit.apply()
            mAuth.signOut()
            val intent = Intent(
                requireContext(), HomeAuth::class.java
            )
            startActivity(intent)
            requireActivity().finish()
        }

        binding.viewAllPlans.setOnClickListener {
            viewModel.sendUserToViewPlanActivity()
        }

        binding.currentPlanSettings.setOnClickListener {
            val currentPlan = Plan(
                currentPlanPref.getString("PlanName", "") ?: "",
                currentPlanPref.getString("PlanDesc", "") ?: "",
                currentPlanPref.getString("PlanTimeNumber", "") ?: "",
                currentPlanPref.getString("PlanFees", "") ?: "",
                currentPlanPref.getBoolean("PlanPT", false),
                currentPlanPref.getString("PlanKey", "") ?: "",
                currentPlanPref.getInt("PlanTotalDays", 0)
            )
            planName.text=currentPlan.name
            dialog.show() // Showing the dialog here

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