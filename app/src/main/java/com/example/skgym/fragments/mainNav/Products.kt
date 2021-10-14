package com.example.skgym.fragments.mainNav

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.skgym.Adapters.CategoriesAdapter
import com.example.skgym.activities.ViewProducts
import com.example.skgym.data.ProductCategory
import com.example.skgym.databinding.FragmentProductsBinding
import com.example.skgym.di.component.DaggerFactoryComponent
import com.example.skgym.di.modules.FactoryModule
import com.example.skgym.di.modules.RepositoryModule
import com.example.skgym.mvvm.repository.MainRepository
import com.example.skgym.mvvm.viewmodles.MainViewModel


class Products : Fragment() {

    private var _binding: FragmentProductsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel
    private var arrayListProductCat = arrayListOf<ProductCategory>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductsBinding.inflate(inflater, container, false)
        init()



        binding.fabMain.setOnClickListener {

        }

        viewModel.allCategories.observe(requireActivity()) {
            arrayListProductCat = it
            val categoriesAdapter = CategoriesAdapter(requireContext(), arrayListProductCat)
            binding.gridView.adapter = categoriesAdapter
            binding.progressBarProductsCatLoad.visibility = View.GONE
        }

        binding.gridView.setOnItemClickListener { _, _, position, _ ->
            val intent = Intent(requireContext(), ViewProducts::class.java)
            intent.putExtra("Category", arrayListProductCat[position])
            startActivity(intent)
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

    }


}