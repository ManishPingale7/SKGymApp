package com.example.skgym.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.example.skgym.R
import com.example.skgym.databinding.FragmentHomeBinding
import com.example.skgym.databinding.FragmentViewPlansBinding
import com.example.skgym.di.component.DaggerFactoryComponent
import com.example.skgym.di.modules.FactoryModule
import com.example.skgym.di.modules.RepositoryModule
import com.example.skgym.mvvm.repository.MainRepository
import com.example.skgym.mvvm.viewmodles.MainViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class ViewPlans : Fragment() {
    private lateinit var viewModel: MainViewModel
    private var _binding: FragmentViewPlansBinding? = null
    var branch = ""
    private val binding get() = _binding!!
    lateinit var currentUser: FirebaseUser
    var mAuth = FirebaseAuth.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentViewPlansBinding.inflate(inflater, container, false)

        init()

        return binding.root
    }

    private fun init() {
        val component: DaggerFactoryComponent = DaggerFactoryComponent.builder()
            .repositoryModule(RepositoryModule(requireContext()))
            .factoryModule(FactoryModule(MainRepository(requireContext())))
            .build() as DaggerFactoryComponent
        viewModel =
            ViewModelProviders.of(this@ViewPlans, component.getFactory())
                .get(MainViewModel::class.java)
        mAuth = FirebaseAuth.getInstance()

    }

}