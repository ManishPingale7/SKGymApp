package com.example.skgym.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.example.skgym.R
import com.example.skgym.data.Plan
import com.example.skgym.databinding.ActivityCheckoutBinding
import com.example.skgym.di.component.DaggerFactoryComponent
import com.example.skgym.di.modules.FactoryModule
import com.example.skgym.di.modules.RepositoryModule
import com.example.skgym.mvvm.repository.MainRepository
import com.example.skgym.mvvm.viewmodles.MainViewModel
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import org.json.JSONObject


class CheckoutActivity : AppCompatActivity(), PaymentResultListener {
    private lateinit var viewModel: MainViewModel
    private lateinit var component: DaggerFactoryComponent
    private lateinit var checkout: Checkout
    private var demo: Plan? = null

    lateinit var binding: ActivityCheckoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        demo = intent.getParcelableExtra("Plan")
        init()


        setPlanLayout(demo)
        binding.checkoutBtn.setOnStateChangeListener {
            setPayment()

        }
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

    private fun setPlanLayout(demo: Plan?) {
        demo?.let {
            binding.cardPlanNameD.text = demo.name
            binding.cardPlanDescText.text = demo.desc
            if (demo.pt == true) {
                binding.isPersonalD.text = "PT"
            } else {
                binding.isPersonalD.text = "Normal"
                binding.badgeGold.visibility = View.GONE

            }
            binding.cardFeesD.text = demo.fees
            binding.subtotal.text = demo.fees
            binding.totalFees.text = demo.fees
            binding.cardDurationD.text=demo.timeNumber
        }
    }

    private fun setPayment() {
        Log.d("TAG111", "setPayment: here")
        checkout = Checkout()
        checkout.setKeyID("rzp_test_MbMaA0scjOVfmP")

        val jsonObject = JSONObject()
        jsonObject.put("name", "BK Gym Membership")
        jsonObject.put("theme.color", "#0093DD")
        jsonObject.put("currency", "INR")
        demo?.let {
            jsonObject.put("amount", (demo!!.fees).toInt() * 100)
        }

        checkout.open(this, jsonObject)
    }

    override fun onPaymentSuccess(p0: String?) {
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
    }

    override fun onPaymentError(int: Int, p1: String?) {
        Toast.makeText(this, "Failed: $p1", Toast.LENGTH_SHORT).show()

    }
}