package com.example.svbookmarket.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.svbookmarket.activities.adapter.OrderAdapter
import com.example.svbookmarket.activities.viewmodel.UserOrderViewModel
import com.example.svbookmarket.databinding.ActivityUserOrderBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserOrder : AppCompatActivity() {

    private lateinit var binding: ActivityUserOrderBinding
    private val viewModel: UserOrderViewModel by viewModels()
    private val orderListAdapter: OrderAdapter = OrderAdapter(mutableListOf(), this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getOrder()
        setUpView()
        binding.backButton.setOnClickListener {
            onBackPressed()
        }

    }

    private fun getOrder() {
        viewModel.orders.observe(this) { changes ->
            orderListAdapter.addOrder(changes)
        }
    }

    private fun setUpView() {
        binding.listOrder.apply {
            adapter = orderListAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }


}