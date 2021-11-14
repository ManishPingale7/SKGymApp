package com.example.skgym.activities

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.skgym.R
import com.example.skgym.Room.viewmodels.CartViewModel
import com.example.skgym.auth.HomeAuth
import com.example.skgym.databinding.ActivityMainBinding
import com.example.skgym.di.component.DaggerFactoryComponent
import com.example.skgym.di.modules.FactoryModule
import com.example.skgym.di.modules.RepositoryModule
import com.example.skgym.mvvm.repository.MainRepository
import com.example.skgym.mvvm.viewmodles.MainViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.razorpay.PaymentResultListener
import java.text.DateFormat


class MainActivity : AppCompatActivity(), PaymentResultListener {

    private lateinit var mAuth: FirebaseAuth
    private var currentuser: FirebaseUser? = null
    private lateinit var viewModel: MainViewModel
    private lateinit var component: DaggerFactoryComponent

    lateinit var binding: ActivityMainBinding
    private lateinit var cartViewModel: CartViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()

        setSupportActionBar(binding.toolbarMain)
        val actionBar: ActionBar? = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        actionBar.setHomeButtonEnabled(true)
        val navController = findNavController(R.id.ContainerViewMain)
        val appBarConfigration = AppBarConfiguration(
            setOf(
                R.id.homeFrag,
                R.id.products,
                R.id.nav_Cart,
                R.id.nav_settings
            )
        )
        setupActionBarWithNavController(navController, appBarConfigration)
        binding.bottomNavigation.setupWithNavController(navController)

    }


    private fun init() {
        val window: Window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.my_statusbar_color)
        mAuth = FirebaseAuth.getInstance()
        component = DaggerFactoryComponent.builder()
            .repositoryModule(RepositoryModule(this))
            .factoryModule(FactoryModule(MainRepository(this)))
            .build() as DaggerFactoryComponent
        viewModel = ViewModelProviders.of(this, component.getFactory())
            .get(MainViewModel::class.java)

        cartViewModel =
            ViewModelProviders.of(this).get(CartViewModel::class.java)

        currentuser = mAuth.currentUser

        checkUser()
    }

    private fun checkUser() {
        mAuth = FirebaseAuth.getInstance()
        currentuser = mAuth.currentUser
        if (currentuser == null) {
            sendUserToHomeActivity()
        }

    }

    private fun sendUserToHomeActivity() {
        Intent(this, HomeAuth::class.java).also {
            startActivity(it)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            finish()
        }
    }

    override fun onPaymentSuccess(msg: String?) {
        pushDataToDb()
        Toast.makeText(this, "Order Successful", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, MainActivity::class.java))
    }

    private fun pushDataToDb() {
        Log.d(TAG, "pushDataToDb: PUSHING DATA!")
        cartViewModel.readUnpaidData.observe(this) {
            Log.d(TAG, "pushDataToDb: DATA WHICH IS BEING PUSHED: $it")
            for (i in it) {
                cartViewModel.pushOrdersToDb(
                    i.copy(
                        paymentDone = true,
                        purchasedAt = DateFormat.getDateInstance()
                            .format(System.currentTimeMillis()),
                    )
                )
            }
        }
    }

    override fun onPaymentError(code: Int, msg: String?) {
        Toast.makeText(this, "Payment Failed", Toast.LENGTH_SHORT).show()
        Log.d("TAG", "onPaymentError: $code and $msg")
    }
}