package com.example.skgym.Fragments.datacollection


import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
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
import com.google.firebase.storage.FirebaseStorage


class DataStage2 : Fragment() {

    private lateinit var viewModel: MainViewModel
    private var _binding: FragmentDataStage2Binding? = null
    private val binding get() = _binding!!
    lateinit var currentUser: FirebaseUser
    private var mAuth = FirebaseAuth.getInstance()
    private val TAG = "DataStage2"
    private var gender = "male"
    private var branches = ArrayList<String>()
    private var memberThis = Member()
    private var dateTaken = false
    private val storageRef = FirebaseStorage.getInstance().reference
    private val userId = mAuth.uid
    private var downloadUrl: Uri? = null


    private val Args: DataStage2Args by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDataStage2Binding.inflate(inflater, container, false)
        init()
        val bloodGroups = resources.getStringArray(R.array.bloodGroups)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdownitem, bloodGroups)
        binding.bloodgrpData.setAdapter(arrayAdapter)
        val builder =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")

        val picker = builder.build()

        binding.pickDateData.setOnClickListener {
            picker.show(requireActivity().supportFragmentManager, "DATE-PICKER")
        }

        viewModel.fetchBranchNames().observe(viewLifecycleOwner, {

            Log.d(TAG, "onCreateView: Size ${it.size}")
            val arrayAdapter = ArrayAdapter(
                requireContext(), R.layout.dropdownitem,
                it.toArray()

            )
            branches = it
            Log.d(TAG, "onCreateView: Size ${branches.size}")
            arrayAdapter.notifyDataSetChanged()
            binding.branchData.setAdapter(arrayAdapter)
        })

//        binding.pickBloodReport.setOnClickListener {
//            val fileIntent = Intent(Intent.ACTION_GET_CONTENT)
//            fileIntent.type = "application/pdf"
//            startActivityForResult(fileIntent, 21)
//        }

        picker.addOnPositiveButtonClickListener {
            Log.d(TAG, "onCreateView: Header Date =${picker.headerText}")
            Log.d(TAG, "onCreateView: Selection =${picker.selection}")
            val date = picker.headerText
            dateTaken = true
            memberThis.dob = date
        }

        picker.addOnNegativeButtonClickListener {
            Log.d(TAG, "onCreateView: NEGATIVE")
            dateTaken = false
        }
        picker.addOnCancelListener {
            Log.d(TAG, "onCreateView: Cancel")
            dateTaken = false
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

            val bloodGroup = binding.bloodgrpData.text.toString()
            val address = binding.addressData.text.toString()
            val branch = binding.branchData.text.toString()
            if (bloodGroup.isNotEmpty()) {
                if (address.isNotEmpty()) {
                    if (dateTaken) {
                        if (branch.isNotEmpty() && branches.contains(branch)) {
                            memberThis.bloodGroup = bloodGroup
                            memberThis.address = address
                            memberThis.branch = branch
                            memberThis.isMember = false
                            Log.d(TAG, "onCreateView: $memberThis")
                            uploadProfileImage(memberThis.imgUrl.toString())
                            memberThis.imgUrl = downloadUrl.toString()
                            requireActivity().finish()
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Enter a Valid Branch $branch",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    }else{Toast.makeText(requireContext(), "Select Date", Toast.LENGTH_SHORT).show()}
                } else {
                    Toast.makeText(requireContext(), "Enter Address", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "Select BloodGroup", Toast.LENGTH_SHORT).show()
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
        currentUser = mAuth.currentUser!!
        memberThis = Args.member

        Log.d(
            TAG,
            "init: Member Details are \n Name = ${memberThis.name}   \n" +
                    "ImageUri is ${memberThis.imgUrl}"
        )
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        when (requestCode) {
//            21 -> {
//                if (resultCode == RESULT_OK) {
//                    bloodReport = data!!.data!!
//                    memberThis.medicalDocuments = bloodReport.toString()
//                }
//            }
//            else -> {
//                Log.d(TAG, "onActivityResult: ELSE")
//
//            }
//        }
//    }

    private fun uploadProfileImage(imgUrl: String) {
        val uri = imgUrl.toUri()
        val extention = getFileExtention(uri)
        val fileRef = storageRef.child(userId.toString()).child(Constants.PROFILE_IMAGE)

        fileRef.putFile(uri).addOnSuccessListener {
            fileRef.downloadUrl.addOnSuccessListener {
                downloadUrl = it
                memberThis.imgUrl=downloadUrl.toString()
                Log.d(TAG, "uploadProfileImage: $downloadUrl")
                viewModel.uploadUserdata(memberThis)
                viewModel.sendUserToMainActivity()
            }
        }


    }

    private fun getFileExtention(uri: Uri): String {
        val cr = requireContext().contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cr.getType(uri)).toString()
    }
}