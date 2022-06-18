package com.example.svbookmarket

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.svbookmarket.activities.HomeActivity
import com.example.svbookmarket.activities.adapter.OrderAdapter
import com.example.svbookmarket.activities.viewmodel.WaitingForDeliveryViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class WaitingFragment : Fragment() {

    private val viewModel: WaitingForDeliveryViewModel by viewModels()
    private lateinit var noItemLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_waiting, container, false)
        val orderListAdapter: OrderAdapter = OrderAdapter(mutableListOf(), this.requireContext())
        viewModel.setWaitingOrder()
        val waitingOrderView: RecyclerView = view.findViewById(R.id.waitingOrder)

        noItemLayout = view.findViewById(R.id.dontHaveWaitingOrderLayout)
        noItemLayout.visibility = View.GONE
        waitingOrderView.apply {
            adapter = orderListAdapter
            layoutManager = LinearLayoutManager(view.context)
        }
        getOrder(orderListAdapter)
        return view;
    }

    private fun getOrder(orderAdapter: OrderAdapter) {
        viewModel.waitingOrders.observe(this.viewLifecycleOwner, { changes ->
            orderAdapter.addOrder(changes)
            if(changes.size == 0){
                noItemLayout.visibility = View.VISIBLE
            }else{
                noItemLayout.visibility = View.GONE
            }
        })
    }
}