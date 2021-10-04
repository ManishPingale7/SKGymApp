package com.example.skgym.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.skgym.Adapters.ViewPlansAdapter
import com.example.skgym.R
import com.example.skgym.data.Plan
import com.example.skgym.databinding.ActivityViewPlanBinding
import com.example.skgym.di.component.DaggerFactoryComponent
import com.example.skgym.di.modules.FactoryModule
import com.example.skgym.di.modules.RepositoryModule
import com.example.skgym.mvvm.repository.MainRepository
import com.example.skgym.mvvm.viewmodles.MainViewModel
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import org.json.JSONObject


class ViewPlan : AppCompatActivity(), PaymentResultListener {
    private lateinit var viewModel: MainViewModel
    private lateinit var component: DaggerFactoryComponent
    lateinit var binding: ActivityViewPlanBinding
    private var viewPlansAdapter = ViewPlansAdapter()
    private lateinit var plansList: ArrayList<Plan>
    private lateinit var checkout: Checkout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkout = Checkout()
        binding = ActivityViewPlanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            recyclerViewPlans.apply {
                adapter = viewPlansAdapter
                layoutManager = LinearLayoutManager(this@ViewPlan)
                setHasFixedSize(true)
            }
        }

        init()
        setupRecycler()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setupRecycler() {
        viewModel.getAllPlans().observe(this) {
            plansList = it
            viewPlansAdapter.submitList(plansList)
            Log.d("TAG", "init:$it ")
            viewPlansAdapter.notifyDataSetChanged()
        }
        viewPlansAdapter.setOnItemClickListener(object : ViewPlansAdapter.onItemClickedListener {
            override fun onItemClicked(position: Int) {
//                setPayment()

                val intent = Intent(this@ViewPlan, CheckoutActivity::class.java)
                intent.putExtra("Plan", plansList[position])
                startActivity(intent)
            }

        })
    }

    private fun setPayment() {
        checkout.setKeyID("rzp_test_MbMaA0scjOVfmP")

        val jsonObject = JSONObject()
        jsonObject.put("name", "Test Payment")
        jsonObject.put("description", "This is the description")
        jsonObject.put("theme.color", "#0093DD")
        jsonObject.put("currency", "INR")
        jsonObject.put("amount", 6900)
        jsonObject.put("prefill.contact", 9142662000)
        jsonObject.put("prefill.email", "adityakadlag2004@gmail.com")

        checkout.open(this@ViewPlan, jsonObject)
    }

    private fun init() {
        val window: Window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.my_statusbar_color)
        component = DaggerFactoryComponent.builder()
            .repositoryModule(RepositoryModule(this))
            .factoryModule(FactoryModule(MainRepository(this)))
            .build() as DaggerFactoryComponent
        viewModel = ViewModelProviders.of(this, component.getFactory())
            .get(MainViewModel::class.java)


    }

    override fun onPaymentSuccess(p0: String?) {
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
    }

    override fun onPaymentError(int: Int, p1: String?) {
        Toast.makeText(this, "Failed: $p1", Toast.LENGTH_SHORT).show()

    }
}