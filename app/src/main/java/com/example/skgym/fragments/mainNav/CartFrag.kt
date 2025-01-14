package com.example.skgym.fragments.mainNav

import android.annotation.SuppressLint
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
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
import com.example.skgym.Interfaces.GetNameCallback
import com.example.skgym.Room.viewmodels.CartViewModel
import com.example.skgym.data.Cart
import com.example.skgym.data.Product
import com.example.skgym.databinding.FragmentCartBinding
import com.example.skgym.utils.Constants
import com.google.gson.Gson
import com.razorpay.Checkout
import org.json.JSONObject


private const val TAG = "CartFragment"

class CartFrag : Fragment() {
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
        checkout.setKeyID(Constants.KEY_ID)

        val jsonObject = JSONObject()
        jsonObject.put("name", "Supplements")
        jsonObject.put("theme.color", "#0093DD")
        jsonObject.put("currency", "INR")
        jsonObject.put("amount", totalPrice * 100)


        checkout.open(requireActivity(), jsonObject)
//        requireActivity().supportFragmentManager.beginTransaction().replace(R.id.)
    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    private fun loadData() {
        viewModel.readUnpaidData.observe(requireActivity()) {
            if (it.isEmpty()) {
                binding.apply {
                    cartPrice.text = "Rs 0"
                    emptyCartLay.visibility = View.VISIBLE
                    recyclerViewCartItems.visibility = View.GONE
                }
            } else {
                Log.d(TAG, "loadData:Room Data $it")
                binding.cartPrice.text = "Rs ${calculateTotalPrice(it)}"
                cartAdapter.submitList(it)
                cartAdapter.notifyDataSetChanged()
            }
        }

    }

    private fun calculateTotalPrice(list: List<Cart>): String {
        totalPrice = 0
        for (i in list)
            totalPrice += gson.fromJson(i.product, Product::class.java).price.toInt() * i.quantity

        return totalPrice.toString()
    }

    private fun init() {
        val prefs = requireContext().getSharedPreferences("Prefs", MODE_PRIVATE)
        val userBranch: SharedPreferences =
            requireActivity().getSharedPreferences("userBranch", MODE_PRIVATE)

        //ViewModel stuff
        viewModel =
            ViewModelProviders.of(requireActivity()).get(CartViewModel::class.java)

        //getting userName
        viewModel.getUserName(userBranch.getString("userBranch", "Null").toString(),
            object : GetNameCallback {
                override fun getName(name: String) {
                    prefs.edit().putString("Name", name).apply()
                }
            })

        //RecyclerView stuff
        cartAdapter = CartAdapter(requireActivity())

        binding.apply {
            recyclerViewCartItems.apply {
                adapter = cartAdapter
                layoutManager = LinearLayoutManager(requireActivity())
                setHasFixedSize(true)
            }
        }

        cartAdapter.setOnClickListener(object : CartAdapter.onItemClickedListener {
            override fun onMinusItemClicked(cart: Cart) {
                if (cart.quantity != 1)
                    viewModel.decreaseQuantity(cart)
                else
                    viewModel.deleteProduct(cart)
            }

            override fun onPlusItemClicked(cart: Cart) {
                viewModel.increaseQuantity(cart)
            }
        })
    }
}