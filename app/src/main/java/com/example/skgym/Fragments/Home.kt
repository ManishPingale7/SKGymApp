package com.example.skgym.Fragments


import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
    var branch=""
    private val binding get() = _binding!!
    lateinit var currentUser: FirebaseUser
    var mAuth = FirebaseAuth.getInstance()

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
//        val editor2 = isBranchTaken.edit()
//        val editor3=userBranch.edit()

        val isBTaken = isBranchTaken.getBoolean("isBranchTaken", false)

        if (isBTaken) {
            binding.becomeMember.text = resources.getString(R.string.become_a_member)
            branch= userBranch.getString("userBranch","").toString()
            Toast.makeText(requireContext(), branch, Toast.LENGTH_SHORT).show()
        } else {
            binding.becomeMember.text = resources.getString(R.string.selectBranch)
        }

        binding.becomeMember.setOnClickListener {
            val text=binding.becomeMember.text.toString()
            if (text == resources.getString(R.string.become_a_member)){

            }else if (text==resources.getString(R.string.selectBranch)){
                val intent= Intent(requireContext(),GetBranch::class.java)
                startActivity(intent)
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

    }


}