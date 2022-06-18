package com.example.svbookmarket.activities.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.svbookmarket.R
import com.example.svbookmarket.activities.CancelOrder
import com.example.svbookmarket.activities.ProfileActivity
import com.example.svbookmarket.activities.common.AppUtil
import com.example.svbookmarket.activities.model.Order
import java.text.DecimalFormat

class OrderAdapter(
    var listOder: MutableList<Order>,
    var context: Context
): RecyclerView.Adapter<OrderAdapter.ViewHolder>() {
    class ViewHolder (view: View): RecyclerView.ViewHolder(view){
        val status: TextView = view.findViewById(R.id.status)
        val dateTime: TextView = view.findViewById(R.id.dateTime)
        val name: TextView = view.findViewById(R.id.orderName)
        val address: TextView = view.findViewById(R.id.orderAddress)
        val phone: TextView = view.findViewById(R.id.orderPhoneNumber)
        val listItemOrder: RecyclerView = view.findViewById(R.id.orderItemBill)
        val totalPrice: TextView = view.findViewById(R.id.orderSum)
        val expandAddress: Button = view.findViewById(R.id.expandAddress)
        val expandBill: Button = view.findViewById(R.id.expandBill)
        val addressLayout: LinearLayout = view.findViewById(R.id.addressLayout)
        val cancelOrderLayout: ConstraintLayout = view.findViewById(R.id.cancelOrder)
        val cancelOrderButton: Button = view.findViewById(R.id.cancelOrderButton)
        val sellerName: TextView = view.findViewById(R.id.seller)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_order, parent, false)
        return ViewHolder(view)
    }
    fun addOrder(change: MutableList<Order>){
        if (this.listOder.isNotEmpty()) {
            this.listOder.clear()
        }
        this.listOder.addAll(change)
        notifyDataSetChanged()
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentOrder: Order = listOder[position]
        holder.apply {
            if(currentOrder.status == "WAITING" || currentOrder.status == "CONFIRMED"){
                cancelOrderLayout.visibility = View.VISIBLE
            }
            sellerName.text = currentOrder.seller
            status.text = currentOrder.status
            name.text = currentOrder.userDeliverAddress.fullName
            phone.text = currentOrder.userDeliverAddress.phoneNumber
            address.text = currentOrder.userDeliverAddress.addressLane +", "+ currentOrder.userDeliverAddress.district +", "+ currentOrder.userDeliverAddress.city+"."
            dateTime.text = currentOrder.dateTime
            val formatter = DecimalFormat("#,###")
            totalPrice.text = formatter.format(currentOrder.totalPrince.toString().toLong()) +" đ"
           // val billingItemAdapter = BillingItemAdapter(currentOrder.listbooks)
            //listItemOrder.adapter = billingItemAdapter
            listItemOrder.layoutManager = LinearLayoutManager(context)
            listItemOrder.visibility = View.GONE
            addressLayout.visibility = View.GONE
            expandAddress.setOnClickListener {
                onExpandAddressClick(addressLayout, expandAddress)
            }
            expandBill.setOnClickListener {
                onExpandBillClick(listItemOrder, expandBill)
            }
            cancelOrderButton.setOnClickListener {
                AppUtil.currentOrder = currentOrder
               gotoOrderCancel()
            }
            sellerName.setOnClickListener {
                AppUtil.currentSeller.email = currentOrder.sellerId
                startActivity(context,Intent(context, ProfileActivity::class.java), Bundle())
            }

        }
    }
    private fun gotoOrderCancel(){
        startActivity(context,Intent(context, CancelOrder::class.java), Bundle())
    }
    private fun onExpandBillClick(listItemOrder: RecyclerView, expandButton: Button){
        if(listItemOrder.visibility == View.GONE){
            listItemOrder.visibility = View.VISIBLE
            expandButton.setBackgroundResource(R.drawable.ic_baseline_expand_less_24)

        }else{
            listItemOrder.visibility = View.GONE
            expandButton.setBackgroundResource(R.drawable.ic_baseline_expand_more_24)
        }
    }

    private fun onExpandAddressClick(addressLayout: LinearLayout, expandButton: Button){
        if(addressLayout.visibility == View.GONE){
            addressLayout.visibility = View.VISIBLE
            expandButton.setBackgroundResource(R.drawable.ic_baseline_expand_less_24)

        }else{
            addressLayout.visibility = View.GONE
            expandButton.setBackgroundResource(R.drawable.ic_baseline_expand_more_24)
        }
    }

    override fun getItemCount(): Int {
        return listOder.size
    }


}