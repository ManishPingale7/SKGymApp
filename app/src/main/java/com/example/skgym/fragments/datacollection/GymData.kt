package com.example.skgym.fragments.datacollection


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import com.example.skgym.Interfaces.BranchInterface
import com.example.skgym.Interfaces.DataAdded
import com.example.skgym.R
import com.example.skgym.activities.MainActivity
import com.example.skgym.data.Member
import com.example.skgym.databinding.GymdataBinding
import com.example.skgym.di.component.DaggerFactoryComponent
import com.example.skgym.di.modules.FactoryModule
import com.example.skgym.di.modules.RepositoryModule
import com.example.skgym.mvvm.repository.MainRepository
import com.example.skgym.mvvm.viewmodles.MainViewModel
import com.example.skgym.utils.ProgressBtn
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class GymData : Fragment() {

    private lateinit var viewModel: MainViewModel
    private var _binding: GymdataBinding? = null
    private val binding get() = _binding!!
    lateinit var currentUser: FirebaseUser
    private var mAuth = FirebaseAuth.getInstance()
    private val TAG = "DataStage2"
    private var gender = "male"
    private var branches = ArrayList<String>()
    private var memberThis = Member()
    private lateinit var progressBtn: ProgressBtn
    private val argsData: GymDataArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = GymdataBinding.inflate(inflater, container, false)
        init()

        val userBranch: SharedPreferences =
            requireActivity().getSharedPreferences("userBranch", Context.MODE_PRIVATE)
        val branch = userBranch.getString("userBranch", "").toString()

        if (branch.isNotEmpty())
            binding.branchData.setText(branch)

        val view = binding.root.findViewById<View>(R.id.btn_continue_datastage2)

        progressBtn = ProgressBtn(requireContext(), view)

        viewModel.fetchBranchNames(object : BranchInterface {
            override fun getBranch(value: ArrayList<String>) {
                Log.d(TAG, "getBranch: Branch Loaded")
            }

        }).observe(viewLifecycleOwner) {
            Log.d(TAG, "onCreateView: Size ${it.size}")
            val arrayAdapter = ArrayAdapter(
                requireContext(), R.layout.dropdownitem,
                it.toArray()
            )
            branches = it
            Log.d(TAG, "onCreateView: Size ${branches.size}")
            arrayAdapter.notifyDataSetChanged()
            binding.branchData.setAdapter(arrayAdapter)
        }

        when (binding.radioGroupGender.checkedRadioButtonId) {
            R.id.radio_button_male -> {
                gender = "male"
                memberThis.gender = gender
            }
            R.id.radio_button_female -> {
                gender = "female"
                memberThis.gender = gender
            }
        }
        Log.d(TAG, "onCreateView: Gender $gender")
        binding.radioButtonMale.setOnCheckedChangeListener { _, _ ->
            if (binding.radioButtonMale.isChecked) {
                gender = "male"
                memberThis.gender = gender
            }
            Log.d(TAG, "onCreateView: Gender $gender")
        }
        binding.radioButtonFemale.setOnCheckedChangeListener { _, _ ->
            if (binding.radioButtonFemale.isChecked) {
                gender = "female"
                memberThis.gender = gender
            }
            Log.d(TAG, "onCreateView: Gender $gender")
        }
        view.setOnClickListener {

            val branch = binding.branchData.text.toString()
            if (branch.isNotEmpty() && branches.contains(branch)) {
                progressBtn.buttonActivated()
                memberThis.branch = branch
                memberThis.member = false
                Log.d(TAG, "onCreateView: $memberThis")
                viewModel.uploadUserdata(memberThis, object : DataAdded {
                    override fun dataAdded(added: Boolean) {
                        if (added) {
                            startActivity(Intent(requireContext(), MainActivity::class.java))
                            requireActivity().finish()
                        }
                    }
                })
                Toast.makeText(
                    requireContext(),
                    "Please Wait while uploading your data",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Enter a Valid Branch $branch",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }


        }


        return binding.root
    }


    private fun init() {
        val window: Window = requireActivity().window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.my_statusbar_color)
        val component: DaggerFactoryComponent = DaggerFactoryComponent.builder()
            .repositoryModule(RepositoryModule(requireContext()))
            .factoryModule(FactoryModule(MainRepository(requireContext())))
            .build() as DaggerFactoryComponent
        viewModel =
            ViewModelProviders.of(this, component.getFactory()).get(MainViewModel::class.java)
        mAuth = FirebaseAuth.getInstance()
        currentUser = mAuth.currentUser!!
        memberThis = argsData.member
    }


}