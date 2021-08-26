package com.example.skgym.fragments


import android.content.ContentValues.TAG
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.skgym.R
import com.example.skgym.activities.GetBranch
import com.example.skgym.databinding.FragmentHomeBinding
import com.example.skgym.di.component.DaggerFactoryComponent
import com.example.skgym.di.modules.FactoryModule
import com.example.skgym.di.modules.RepositoryModule
import com.example.skgym.mvvm.repository.MainRepository
import com.example.skgym.mvvm.viewmodles.MainViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class Home : Fragment() {
    private lateinit var viewModel: MainViewModel
    private var _binding: FragmentHomeBinding? = null
    var branch = ""
    private val binding get() = _binding!!
    lateinit var currentUser: FirebaseUser
    var mAuth = FirebaseAuth.getInstance()
    var isDatatakenB = false
    var isMem = false
    var isMemChecked = false



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        init()
        //Is branch Taken
        val isBranchTaken: SharedPreferences =
            requireActivity().getSharedPreferences("isBranchTaken", MODE_PRIVATE)
        val userBranch: SharedPreferences =
            requireActivity().getSharedPreferences("userBranch", MODE_PRIVATE)
        val isDataTaken: SharedPreferences =
            requireActivity().getSharedPreferences("isDataTaken", MODE_PRIVATE)
        val isUserMemberChecked: SharedPreferences =
            requireActivity().getSharedPreferences("isUserMemberChecked", MODE_PRIVATE)
        val isUserMemberCheckedEdit = isUserMemberChecked.edit()
        val isUserMember: SharedPreferences =
            requireActivity().getSharedPreferences("isUserMember", MODE_PRIVATE)
        val isUserMemberEdit = isUserMember.edit()

        isMem = isUserMember.getBoolean("isUserMember", false)
        isMemChecked = isUserMemberChecked.getBoolean("isUserMemberChecked", false)
        isDatatakenB = isDataTaken.getBoolean("isDataTaken", false)


        val isBTaken = isBranchTaken.getBoolean("isBranchTaken", false)

        if (isBTaken) {
            Log.d(TAG, "onCreateView: Branch Taken")
            binding.becomeMember.text = resources.getString(R.string.become_a_member)
            branch = userBranch.getString("userBranch", "").toString()
            if (!isDatatakenB) {
                Log.d(TAG, "onCreateView: Not data taken Checking data and branch Exists")
                viewModel.isBranchExists(branch)
            } else {
                binding.progressBarHome.visibility = View.GONE
                Log.d(TAG, "onCreateView: Data Taken")
                if (isMemChecked) {
                    Log.d(TAG, "onCreateView: Checked Member")
                    if (isMem) {
                        binding.nomemberLayout.visibility = View.GONE
                        binding.progressBarHome.visibility = View.GONE
                        Log.d(TAG, "onCreateView: Member")
                    } else {
                        binding.nomemberLayout.visibility = View.VISIBLE
                        binding.progressBarHome.visibility = View.GONE
                        Log.d(TAG, "onCreateView: No Member")
                    }
                } else {
                    Log.d(TAG, "onCreateView: Checking Member")
                    val mem = viewModel.checkUserIsMember(branch)
                    if (mem) {
                        isUserMemberCheckedEdit.putBoolean("isUserMemberChecked", true)
                        isUserMemberCheckedEdit.apply()
                        isUserMemberEdit.putBoolean("isUserMember", true)
                        isUserMemberEdit.apply()
                    } else {
                        isUserMemberCheckedEdit.putBoolean("isUserMemberChecked", true)
                        isUserMemberCheckedEdit.apply()
                    }
                }
            }
        } else {
            binding.becomeMember.text = resources.getString(R.string.selectBranch)
            val intent = Intent(requireContext(), GetBranch::class.java)
            startActivity(intent)
        }





        binding.becomeMember.setOnClickListener {
            viewModel.sendUserToViewPlanActivity()
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
        if (mAuth.currentUser == null) {
            viewModel.sendUserToHomeAuth()
        }
    }


}