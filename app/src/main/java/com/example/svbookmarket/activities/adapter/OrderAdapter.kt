package com.example.svbookmarket.activities.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.svbookmarket.R
import com.example.svbookmarket.activities.CancelOrder
import com.example.svbookmarket.activities.common.AppUtil
import com.example.svbookmarket.activities.common.Constants
import com.example.svbookmarket.activities.model.Order
import com.example.svbookmarket.activities.model.OrderBankLoansIdentify
import com.example.svbookmarket.activities.model.OrderStudentIdentify

class OrderAdapter(
    var listOder: MutableList<Order>,
    var context: Context
) : RecyclerView.Adapter<OrderAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val status: TextView = view.findViewById(R.id.status)
        val dateTime: TextView = view.findViewById(R.id.dateTime)
        val orderKind: TextView = view.findViewById(R.id.orderKind)
        val reason: TextView = view.findViewById(R.id.reason)
        val tuitionKind: TextView = view.findViewById(R.id.tuitionKind)
        val familyKind: TextView = view.findViewById(R.id.familyKind)
        val cancelOrderLayout: ConstraintLayout = view.findViewById(R.id.cancelOrder)
        val cancelOrderButton: Button = view.findViewById(R.id.cancelOrderButton)
        val cancelReasonLayout: RelativeLayout = view.findViewById(R.id.cancelReasonLayout)
        val cancelReason: TextView = view.findViewById(R.id.cancelReason)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_order, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addOrder(change: MutableList<Order>) {
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
            status.text = currentOrder.status
            dateTime.text = currentOrder.dateTime
            orderKind.text = currentOrder.kind
            if (currentOrder.kind == Constants.OrderKind.GXNSV.toString()) {
                // update view
                reason.visibility = View.VISIBLE
                tuitionKind.visibility = View.GONE
                familyKind.visibility = View.GONE
                // set data
                reason.text = "Lý do xác nhận: ${(currentOrder as OrderStudentIdentify).reason}"

            } else {
                // update view
                reason.visibility = View.GONE
                tuitionKind.visibility = View.VISIBLE
                familyKind.visibility = View.VISIBLE
                // set data
                tuitionKind.text = "Thuộc diện: ${ (currentOrder as OrderBankLoansIdentify).tuitionKind}"
                familyKind.text =  "Thuộc đối tượng: ${ (currentOrder).familyKind}"
            }
            if (currentOrder.status == Constants.OrderStatus.CANCEL.toString()) {
                // update view
                cancelReasonLayout.visibility = View.VISIBLE
                status.setTextColor(Color.GRAY)
                // set data
                cancelReason.text = currentOrder.cancelReason
            } else {
                // update view
                cancelReasonLayout.visibility = View.GONE
            }
            if (currentOrder.status == Constants.OrderStatus.WAITING.toString() || currentOrder.status == Constants.OrderStatus.CONFIRMED.toString()) {
                cancelOrderLayout.visibility = View.VISIBLE
                cancelOrderButton.setOnClickListener {
                    AppUtil.currentOrder = currentOrder
                    gotoOrderCancel()
                }
            }
        }
    }

    private fun gotoOrderCancel() {
        startActivity(context, Intent(context, CancelOrder::class.java), Bundle())
    }


    override fun getItemCount(): Int {
        return listOder.size
    }


}