package com.example.svbookmarket.activities.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.svbookmarket.activities.model.Cart
import com.example.svbookmarket.databinding.ItemBillingBinding

class BillingItemAdapter(
    var billingItem: ArrayList<Cart>
): RecyclerView.Adapter<BillingItemAdapter.ViewHolder>() {
    class ViewHolder(binding: ItemBillingBinding): RecyclerView.ViewHolder(binding.root){
        val itemName = binding.itName
        val itemPrice = binding.itPrice
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemBillingBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }
    fun onChange(newItems: ArrayList<Cart>) {
        billingItem = newItems
        notifyDataSetChanged()
    }
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemPrice.text =
            (billingItem[position].price * billingItem[position].numbers).toString() + " đ"
        holder.itemName.text = billingItem[position].name
        Log.d("modal", "created")
    }

    override fun getItemCount(): Int {
        return billingItem.size
    }
}