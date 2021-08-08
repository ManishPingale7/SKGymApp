

package com.example.skgym.Fragments.datacollection

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.example.skgym.data.Member
import com.example.skgym.databinding.FragmentGeneralDataBinding
import com.example.skgym.di.component.DaggerFactoryComponent
import com.example.skgym.di.modules.FactoryModule
import com.example.skgym.di.modules.RepositoryModule
import com.example.skgym.mvvm.repository.MainRepository
import com.example.skgym.mvvm.viewmodles.MainViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class GeneralData : Fragment() {

    private lateinit var viewModel: MainViewModel
    private var _binding: FragmentGeneralDataBinding? = null
    private val binding get() = _binding!!
    var imageUri: Uri? = null
    lateinit var currentUser: FirebaseUser
    var mAuth = FirebaseAuth.getInstance()
    var member = Member()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGeneralDataBinding.inflate(inflater, container, false)
        init()
        binding.addProfileImage.setOnClickListener {
            pickImage()
        }

        binding.profileImageGeneral.setOnClickListener {
            pickImage()
        }

        binding.btnContinueGeneral1.setOnClickListener {
            val firstname = binding.firstNameGeneral.text.toString()
            val middleName = binding.middleNameGeneral.text.toString()
            val lastname = binding.lastNameGeneral.text.toString()

            if (firstname.isNotEmpty() && middleName.isNotEmpty() && lastname.isNotEmpty()) {
                if (imageUri != null) {
                    member.firstname = firstname
                    member.middleName = middleName
                    member.lastname = lastname
                    member.imgUrl = imageUri
                    //Passing Data
                    val action = GeneralDataDirections.actionGeneralDataToDataStage2(member)
                    it.findNavController().navigate(action)
                } else {
                    Toast.makeText(requireContext(), "Select Profile Image", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                Toast.makeText(requireContext(), "Fill the Fields", Toast.LENGTH_SHORT).show()
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

    }

    private fun pickImage() {
        val galleryIntent = Intent()
        galleryIntent.apply {
            action = Intent.ACTION_GET_CONTENT
            type = "image/*"
            startActivityForResult(galleryIntent, 2)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            imageUri = data.data!!
            binding.profileImageGeneral.setImageURI(imageUri)
        }
    }

}
