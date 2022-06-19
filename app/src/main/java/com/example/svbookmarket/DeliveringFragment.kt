package com.example.svbookmarket

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.svbookmarket.activities.adapter.OrderAdapter
import com.example.svbookmarket.activities.viewmodel.WaitingForDeliveryViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeliveringFragment : Fragment() {

    private val viewModel: WaitingForDeliveryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View= inflater.inflate(R.layout.fragment_delivering, container, false)
        val orderListAdapter: OrderAdapter = OrderAdapter(mutableListOf(), this.requireContext())
        //viewModel.setDeliveringOrder()
        val deliveringOrder: RecyclerView = view.findViewById(R.id.deliveringOrder)
        deliveringOrder.apply {
            adapter = orderListAdapter
            layoutManager = LinearLayoutManager(view.context)
        }
        getOrder(orderListAdapter)


        return view
    }
    private fun getOrder(orderAdapter: OrderAdapter) {
        viewModel.deliveryOrder.observe(this.viewLifecycleOwner, { changes ->
            orderAdapter.addOrder(changes)
        })
    }
}