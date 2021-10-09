package com.example.skgym.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.skgym.Adapters.ProductsAdapter
import com.example.skgym.R
import com.example.skgym.data.ProductCategory
import com.example.skgym.databinding.ActivityViewProductsInCategoryBinding
import com.example.skgym.di.component.DaggerFactoryComponent
import com.example.skgym.di.modules.FactoryModule
import com.example.skgym.di.modules.RepositoryModule
import com.example.skgym.mvvm.repository.MainRepository
import com.example.skgym.mvvm.viewmodles.MainViewModel

class ViewProducts : AppCompatActivity() {
    private lateinit var binding: ActivityViewProductsInCategoryBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var component: DaggerFactoryComponent
    private lateinit var productsAdapter: ProductsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewProductsInCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        loadData()


    }

    private fun init() {
        //Toolbar stuff
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


        //Setting the recycler view
        productsAdapter = ProductsAdapter(this)

        binding.apply {
            recyclerViewProducts.apply {
                adapter = productsAdapter
                layoutManager = LinearLayoutManager(this@ViewProducts)
                setHasFixedSize(true)
            }
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadData() {
        val category = intent.getParcelableExtra<ProductCategory>("Category")
        category?.let {
            viewModel.loadProducts(category.name).observe(this) {
                productsAdapter.submitList(it)
                productsAdapter.notifyDataSetChanged()
            }
        }
    }

}