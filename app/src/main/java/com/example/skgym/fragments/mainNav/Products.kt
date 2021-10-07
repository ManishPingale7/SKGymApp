package com.example.skgym.fragments.mainNav

import android.content.ContentValues.TAG
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.skgym.Adapters.GridAdapter
import com.example.skgym.R
import com.example.skgym.data.ProductCategory
import com.example.skgym.databinding.FragmentProductsBinding
import com.example.skgym.di.component.DaggerFactoryComponent
import com.example.skgym.di.modules.FactoryModule
import com.example.skgym.di.modules.RepositoryModule
import com.example.skgym.mvvm.repository.MainRepository
import com.example.skgym.mvvm.viewmodles.MainViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog


class Products : Fragment() {

    private var _binding: FragmentProductsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel
    private var arrayListProductCat = arrayListOf<ProductCategory>()
    var arrayNames = ArrayList<String>()
    var arrayImages = ArrayList<Uri>()

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

            arrayListProductCat.forEach { it1 ->
                arrayImages.add(it1.image.toUri())
                arrayNames.add(it1.name)
                Log.d(TAG, "onCreateView123: added to array2nd")
            }

            Log.d(TAG, "onCreateView123 45:  HERE IT IS $arrayNames ")

            val gridAdapter = GridAdapter(requireContext(), arrayNames, arrayImages)
            binding.gridView.adapter = gridAdapter
        }

        binding.gridView.setOnItemClickListener { parent, view, position, id ->
            Toast.makeText(context, arrayNames[position], Toast.LENGTH_SHORT).show()
            val bottomSheetDialog=BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)

            val bottomSheetView=LayoutInflater.from(requireContext()).inflate(R.layout.layout_bottom_sheet,
                requireActivity().findViewById(R.id.bottom_sheet_Container))

            bottomSheetDialog.setContentView(bottomSheetView)
            bottomSheetDialog.show()
            bottomSheetView.findViewById<Button>(R.id.AddToCart).setOnClickListener {
                bottomSheetDialog.dismiss()
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

    }


}