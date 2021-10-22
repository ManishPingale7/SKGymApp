package com.example.skgym.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.skgym.Adapters.PlansHisAdapter
import com.example.skgym.R
import com.example.skgym.Room.viewmodels.PlansDbViewModel
import com.example.skgym.databinding.ActivityPlansHistoryBinding

class PlansHistory : AppCompatActivity() {
    lateinit var binding: ActivityPlansHistoryBinding
    private lateinit var plansViewModel: PlansDbViewModel
    private var plansHisAdapter = PlansHisAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlansHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        binding.apply {
            recyclerViewPlansHistory.apply {
                adapter = plansHisAdapter
                layoutManager = LinearLayoutManager(this@PlansHistory)
                setHasFixedSize(true)
            }
        }
        setupRecycler()
    }

    private fun init() {
        val window: Window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.my_statusbar_color)

        plansViewModel = ViewModelProviders.of(this).get(PlansDbViewModel::class.java)

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setupRecycler() {
        plansViewModel.readPlansHistory.observe(this) {
            plansHisAdapter.submitList(it)
            plansHisAdapter.notifyDataSetChanged()
        }
    }
}