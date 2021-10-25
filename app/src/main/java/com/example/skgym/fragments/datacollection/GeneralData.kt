package com.example.skgym.fragments.datacollection

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.example.skgym.R
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
    private var currentUser: FirebaseUser? = null
    var mAuth = FirebaseAuth.getInstance()
    var member = Member()


    @SuppressLint("CommitPrefEdits")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGeneralDataBinding.inflate(inflater, container, false)
        init()
        val userName: SharedPreferences =
            requireActivity().getSharedPreferences("username", Context.MODE_PRIVATE)

        val editor = userName.edit()


        binding.btnContinueGeneral1.setOnClickListener {
            val firstname = binding.firstNameGeneral.text.toString()
            val middleName = binding.middleNameGeneral.text.toString()
            val lastname = binding.lastNameGeneral.text.toString()

            if (firstname.isNotEmpty() && middleName.isNotEmpty() && lastname.isNotEmpty()) {
                member.name = "$firstname $middleName $lastname"
                editor.putString("username", member.name)
                editor.apply()
                val action = GeneralDataDirections.actionGeneralDataToGymData(member)
                it.findNavController().navigate(action)
            } else {
                Toast.makeText(requireContext(), "Fill the Fields", Toast.LENGTH_SHORT).show()
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
        currentUser = mAuth.currentUser

    }
}
