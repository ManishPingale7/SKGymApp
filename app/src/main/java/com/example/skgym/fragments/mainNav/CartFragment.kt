package com.example.skgym.fragments.mainNav

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.skgym.Adapters.CartAdapter
import com.example.skgym.Room.viewmodels.CartViewModel
import com.example.skgym.data.Cart
import com.example.skgym.data.Product
import com.example.skgym.databinding.FragmentCartBinding
import com.google.gson.Gson
import com.razorpay.Checkout
import org.json.JSONObject


private const val TAG = "CartFragment"

class CartFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding
    private lateinit var viewModel: CartViewModel
    private lateinit var cartAdapter: CartAdapter
    private lateinit var checkout: Checkout
    private var totalPrice = 0
    var gson = Gson()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        binding = FragmentCartBinding.inflate(inflater, container, false)
        init()
        loadData()

        binding.proceedToBuy.setOnClickListener {
            if (totalPrice != 0)
                initiatePayment()
            else
                Toast.makeText(requireActivity(), "Your Cart is Empty", Toast.LENGTH_SHORT).show()
        }
        return binding.root
    }

    private fun initiatePayment() {
        checkout = Checkout()
        checkout.setKeyID("rzp_test_MbMaA0scjOVfmP")

        val jsonObject = JSONObject()
        jsonObject.put("name", "Supplements")
        jsonObject.put("theme.color", "#0093DD")
        jsonObject.put("currency", "INR")
        jsonObject.put("amount", totalPrice * 100)


        checkout.open(requireActivity(), jsonObject)
//        requireActivity().supportFragmentManager.beginTransaction().replace(R.id.)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadData() {
        viewModel.readAllData.observe(requireActivity()) {
            Log.d(TAG, "loadData: Room Dataaaaaa $it")
        }

        viewModel.readUnpaidData.observe(requireActivity()) {
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
            ViewModelProviders.of(requireActivity()).get(CartViewModel::class.java)

        //RecyclerView stuff
        cartAdapter = CartAdapter(requireActivity())

        binding.apply {
            recyclerViewCartItems.apply {
                adapter = cartAdapter
                layoutManager = LinearLayoutManager(requireActivity())
                setHasFixedSize(true)
            }
        }
    }


}