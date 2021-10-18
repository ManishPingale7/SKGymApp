package com.example.skgym.fragments.mainNav

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.skgym.Adapters.CartAdapter
import com.example.skgym.R
import com.example.skgym.Room.viewmodels.CartViewModel
import com.example.skgym.data.Cart
import com.example.skgym.data.Product
import com.example.skgym.databinding.FragmentCartBinding
import com.google.gson.Gson


private const val TAG = "CartFragment"

class CartFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding
    private lateinit var viewModel: CartViewModel
    private lateinit var cartAdapter: CartAdapter
    private var totalPrice = 0
    var gson = Gson()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(LayoutInflater.from(context), container, false)
        init()






        loadData()
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadData() {
        viewModel.readAllData.observe(requireActivity()) {
            Log.d(TAG, "loadData:Room Data $it")
            binding.cartPrice.text = calculateTotalPrice(it)
            cartAdapter.submitList(it)
            cartAdapter.notifyDataSetChanged()
        }
    }

    private fun calculateTotalPrice(list: List<Cart>): String {
        totalPrice = 0
        for (i in list)
            totalPrice += gson.fromJson(i.product, Product::class.java).price.toInt() * i.quantity

        return totalPrice.toString()
    }

    private fun init() {
        //ViewModel stuff
        viewModel =
            ViewModelProviders.of(this).get(CartViewModel::class.java)

        //RecyclerView stuff
        cartAdapter = CartAdapter(requireContext())

        binding.apply {
            recyclerViewCartItems.apply {
                adapter = cartAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }
        }
    }
}