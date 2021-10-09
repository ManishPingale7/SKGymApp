package com.example.skgym.activities

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.skgym.Adapters.ProductsAdapter
import com.example.skgym.R
import com.example.skgym.data.Product
import com.example.skgym.data.ProductCategory
import com.example.skgym.databinding.ActivityViewProductsInCategoryBinding
import com.example.skgym.di.component.DaggerFactoryComponent
import com.example.skgym.di.modules.FactoryModule
import com.example.skgym.di.modules.RepositoryModule
import com.example.skgym.mvvm.repository.MainRepository
import com.example.skgym.mvvm.viewmodles.MainViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.progress_btn_layout.*

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
        productsAdapter.setOnItemClickListener(object : ProductsAdapter.onItemClickedListener {
            override fun onItemClicked(product: Product) {
                Toast.makeText(this@ViewProducts, "Clicked ${product.name}", Toast.LENGTH_SHORT)
                    .show()


                val bottomSheetDialog =
                    BottomSheetDialog(this@ViewProducts, R.style.BottomSheetDialogTheme)

                val bottomSheetView = LayoutInflater.from(this@ViewProducts).inflate(
                    R.layout.layout_bottom_sheet,
                    this@ViewProducts.findViewById(R.id.bottom_sheet_Container)
                )

                bottomSheetDialog.setCanceledOnTouchOutside(false)
                bottomSheetDialog.setContentView(bottomSheetView)
                bottomSheetDialog.show()


                val cartBtn = bottomSheetView.findViewById<Button>(R.id.AddToCart)



                bottomSheetView.findViewById<TextView>(R.id.bottomSheetName).text = product.name
                bottomSheetView.findViewById<TextView>(R.id.bottomSheetDesc).text = product.desc



                if (product.flavours != null) {
                    val arrayAdapter = ArrayAdapter(
                        this@ViewProducts, R.layout.dropdownitem,
                        product.flavours!!.toArray()
                    )
                    bottomSheetView.findViewById<AutoCompleteTextView>(R.id.bottomAutoComplete)
                        .setAdapter(arrayAdapter)
                    arrayAdapter.notifyDataSetChanged()

                } else {
                    bottomSheetView.findViewById<AutoCompleteTextView>(R.id.bottomAutoComplete).visibility =
                        View.GONE
                }

                val plusBtn = bottomSheetView.findViewById<ImageView>(R.id.bottomPlusBtn)
                val minusBtn = bottomSheetView.findViewById<ImageView>(R.id.bottomMinusButton)
                val textQuantity =
                    bottomSheetView.findViewById<TextView>(R.id.bottomQuantityTextView)

                minusBtn.setOnClickListener {

                    var number =
                        textQuantity.text.toString()
                            .toInt()
                    if (number > 0) {
                        number--
                        val price = product.price.toInt() * number
                        textQuantity.text =
                            number.toString()
                        if (number != 0) {
                            val text = "${resources.getString(R.string.add)} $price to Cart"
                            cartBtn.text = text
                        } else {
                            cartBtn.text = resources.getString(R.string.add)
                        }

                    }

                    Log.d(TAG, "onItemClicked: $number")
                }

                plusBtn.setOnClickListener {
                    var number =
                        textQuantity.text.toString()
                            .toInt()
                    number++
                    textQuantity.text =
                        number.toString()
                    val price = product.price.toInt() * number
                    val text = "${resources.getString(R.string.add)} $price+ to Cart"
                    cartBtn.text = text
                    Log.d(TAG, "onItemClicked: $number")
                }




                bottomSheetView.findViewById<Button>(R.id.AddToCart).setOnClickListener {
                    bottomSheetDialog.dismiss()
                }


            }
        })

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