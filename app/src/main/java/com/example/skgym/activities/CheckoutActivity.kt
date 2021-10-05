package com.example.skgym.activities

import android.os.Bundle
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


class CheckoutActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var component: DaggerFactoryComponent
    private val TAG = "mActivity"

    lateinit var binding: ActivityCheckoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        init()

        Toast.makeText(
            baseContext,
            intent.getParcelableExtra<Plan>("Plan").toString(),
            Toast.LENGTH_SHORT
        )
            .show()

        binding.checkoutBtn.setOnStateChangeListener { active ->
            Toast.makeText(
                this,
                "State: $active",
                Toast.LENGTH_SHORT
            ).show()
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
}