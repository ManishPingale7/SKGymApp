package com.example.skgym.Fragments.datacollection

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.example.skgym.R
import com.example.skgym.data.Member
import com.example.skgym.databinding.FragmentDataStage2Binding
import com.example.skgym.databinding.FragmentGeneralDataBinding
import com.example.skgym.di.component.DaggerFactoryComponent
import com.example.skgym.di.modules.FactoryModule
import com.example.skgym.di.modules.RepositoryModule
import com.example.skgym.mvvm.repository.MainRepository
import com.example.skgym.mvvm.viewmodles.MainViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class DataStage2 : Fragment() {
    private lateinit var viewModel: MainViewModel
    private var _binding: FragmentDataStage2Binding? = null
    private val binding get() = _binding!!
    lateinit var currentUser: FirebaseUser
    var mAuth = FirebaseAuth.getInstance()
    var member = Member()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDataStage2Binding.inflate(inflater, container, false)
        init()
        val builder =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")

        val picker = builder.build()

        binding.pickDateData.setOnClickListener {
            picker.show(requireActivity().supportFragmentManager, "DATE-PICKER")
        }




        picker.addOnNegativeButtonClickListener {
            // Respond to negative button click.
        }
        picker.addOnCancelListener {
            // Respond to cancel button click.
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

}