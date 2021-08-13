package com.example.skgym.Fragments.datacollection


import android.app.Activity.RESULT_OK
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import com.example.skgym.R
import com.example.skgym.data.Member
import com.example.skgym.databinding.FragmentDataStage2Binding
import com.example.skgym.di.component.DaggerFactoryComponent
import com.example.skgym.di.modules.FactoryModule
import com.example.skgym.di.modules.RepositoryModule
import com.example.skgym.mvvm.repository.MainRepository
import com.example.skgym.mvvm.viewmodles.MainViewModel
import com.example.skgym.utils.Constants
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*


class DataStage2 : Fragment() {

    private lateinit var viewModel: MainViewModel
    private var _binding: FragmentDataStage2Binding? = null
    private val binding get() = _binding!!
    lateinit var currentUser: FirebaseUser
    var mAuth = FirebaseAuth.getInstance()
    private val TAG = "DataStage2"
    var gender = "male"
    lateinit var bloodReport:Uri
    var memberThis = Member()
    private val fDatabase = FirebaseDatabase.getInstance()
    var branchesList = ArrayList<String>()
    var branchesNameRef = fDatabase.getReference(Constants.BRANCHES_SPINNER)


    private val Args: DataStage2Args by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDataStage2Binding.inflate(inflater, container, false)
        init()
        val bloodGroups=resources.getStringArray(R.array.bloodGroups)
        val arrayAdapter= ArrayAdapter(requireContext(),R.layout.dropdownitem,bloodGroups)
        binding.bloodgrpData.setAdapter(arrayAdapter)
        val builder =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")

        val picker = builder.build()

        binding.pickDateData.setOnClickListener {
            picker.show(requireActivity().supportFragmentManager, "DATE-PICKER")
        }

        binding.pickBloodReport.setOnClickListener {
            val fileIntent = Intent(Intent.ACTION_GET_CONTENT)
            fileIntent.type = "application/pdf"
            startActivityForResult(fileIntent, 21)
        }

        picker.addOnPositiveButtonClickListener {
            Log.d(TAG, "onCreateView: Header Date =${picker.headerText}")
            Log.d(TAG, "onCreateView: Selection =${picker.selection}")
            val date = Date(picker.headerText)
            memberThis.dob = date
        }
        picker.addOnNegativeButtonClickListener {
            Log.d(TAG, "onCreateView: NEGATIVE")
        }
        picker.addOnCancelListener {
            Log.d(TAG, "onCreateView: Cancel")

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
        binding.radioButtonMale.setOnCheckedChangeListener { buttonView, isChecked ->
            if (binding.radioButtonMale.isChecked) {
                gender = "male"
                memberThis.gender = gender
            }
            Log.d(TAG, "onCreateView: Gender $gender")
        }
        binding.radioButtonFemale.setOnCheckedChangeListener { buttonView, isChecked ->
            if (binding.radioButtonFemale.isChecked) {
                gender = "female"
                memberThis.gender = gender
            }
            Log.d(TAG, "onCreateView: Gender $gender")
        }

        binding.btnContinueDatastage.setOnClickListener {

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
        currentUser = mAuth.currentUser!!
        memberThis = Args.member
        getBranches()
        Log.d(
            TAG,
            "init: Member Details are \n Name = ${memberThis.firstname} ${memberThis.middleName} ${memberThis.lastname} \n" +
                    "ImageUri is ${memberThis.imgUrl}"
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            21 -> {
                if (resultCode == RESULT_OK) {
                    bloodReport = data!!.data!!
                    memberThis.medicalDocuments=bloodReport
                }
            }
            else -> {
                Log.d(TAG, "onActivityResult: ELSE")

            }
        }
    }


    fun getBranches(){
        branchesNameRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                branchesList.clear()
                for (dataSnapshot: DataSnapshot in snapshot.children) {
                    branchesList.add(dataSnapshot.value.toString())
                    Log.d(ContentValues.TAG, "onDataChange: ${dataSnapshot.value.toString()}")
                    Log.d(ContentValues.TAG, "onDataChange: ${branchesList.size}")
                }
                val branches=branchesList.toArray()
                val arrayAdapter= ArrayAdapter(requireContext(),R.layout.dropdownitem,branches)
                binding.branchData.setAdapter(arrayAdapter)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(ContentValues.TAG, "onCancelled: $error")
            }

        })
    }

}