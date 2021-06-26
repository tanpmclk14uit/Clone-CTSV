package com.example.svbookmarket.activities.adapter

import CurrentUserInfo
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.svbookmarket.activities.model.UserDeliverAddress
import com.example.svbookmarket.databinding.CardAddressBinding
import com.google.android.material.card.MaterialCardView
import kotlin.properties.Delegates


class AddressAdapter(
    private var items: MutableList<UserDeliverAddress>,
    private val listener: NotifyAddressSelectionChanged,
) :
    RecyclerView.Adapter<AddressAdapter.VH>() {
    private var selectedPos = -1

    private var lastSelectedPos = -1

    private var selectedPosition by Delegates.observable(selectedPos) { _, oldPos, newPos ->
        if (newPos in items.indices && newPos != oldPos && oldPos != -1) {
            //update list
            items[oldPos].chose = false
            items[newPos].chose = true

            listener.onAddressChange(items[oldPos], items[newPos])


            //update adapter
            notifyItemChanged(oldPos)
            notifyItemChanged(newPos)


            Log.i("customtag", "old: $oldPos new: $newPos")
        }
    }

    private fun toggleCheckedPosition(oldPos: Int, newPos: Int) {
        if (oldPos == -1 || oldPos == newPos) return
        listener.onAddressChange(items[oldPos], items[newPos])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = CardAddressBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.name.text = items[position].fullName
        holder.phoneNumber.text = items[position].phoneNumber
        holder.lane.text = items[position].addressLane
        holder.district.text = items[position].district
        holder.city.text = items[position].city

        if (items[position].chose) {
            lastSelectedPos = position
        }

        (holder.itemView as MaterialCardView).isChecked = items[position].chose
    }

    fun addAddress(address: List<UserDeliverAddress>) {
        if (this.items.isNotEmpty()) {
            this.items.clear()
        }
        this.items.addAll(address)
        notifyDataSetChanged()
    }


    fun onChange() {
        items = CurrentUserInfo.getInstance().lstUserDeliverAddress
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class VH(binding: CardAddressBinding) : RecyclerView.ViewHolder(binding.root) {

        var name: TextView = binding.tvName
        var phoneNumber: TextView = binding.cardAdPhone
        var lane: TextView = binding.cardAdLane
        var district: TextView = binding.cardAdDistrict
        var city: TextView = binding.cardAdCity
        var root: View = binding.root

        init {
            (root as MaterialCardView).setOnClickListener {
                toggleCheckedPosition(lastSelectedPos, this.adapterPosition)
                lastSelectedPos = this.layoutPosition


            }
        }
    }

    interface NotifyAddressSelectionChanged {
        fun onAddressChange(old: UserDeliverAddress, new: UserDeliverAddress)
    }
}
