package com.example.skgym.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.skgym.Adapters.ProductHistoryAdapter
import com.example.skgym.R
import com.example.skgym.Room.viewmodels.CartViewModel
import com.example.skgym.databinding.ActivityOrdersBinding
import com.example.skgym.di.component.DaggerFactoryComponent
import com.example.skgym.di.modules.FactoryModule
import com.example.skgym.di.modules.RepositoryModule
import com.example.skgym.mvvm.repository.MainRepository
import com.example.skgym.mvvm.viewmodles.MainViewModel


class OrdersActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var component: DaggerFactoryComponent
    private lateinit var historyAdapter: ProductHistoryAdapter
    private lateinit var cartViewModel: CartViewModel

    lateinit var binding: ActivityOrdersBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrdersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()

        loadData()

        binding.goBackHistory.setOnClickListener {
            finish()
        }


    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadData() {
        cartViewModel.readAllData.observe(this@OrdersActivity) {
            Log.d("TAG", "loadData123: $it")
            historyAdapter.submitList(it)
            historyAdapter.notifyDataSetChanged()
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

        cartViewModel = ViewModelProviders.of(this@OrdersActivity).get(CartViewModel::class.java)
        historyAdapter = ProductHistoryAdapter(this@OrdersActivity)


        binding.apply {
            recyclerViewOrdersHis.apply {
                adapter = historyAdapter
                layoutManager = LinearLayoutManager(this@OrdersActivity)
                setHasFixedSize(true)
            }
        }

    }
}