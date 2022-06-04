package com.example.svbookmarket.activities.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.svbookmarket.R
import com.example.svbookmarket.activities.model.Advertise

class AdvertiseAdapter(private val dataSet: MutableList<Advertise>) : RecyclerView.Adapter<AdvertiseAdapter.ViewHolder>() {
    class ViewHolder (view: View): RecyclerView.ViewHolder(view){
        val advertiseImage: ImageView = view.findViewById(R.id.advertiseImage)
        val advertiseInformation: TextView = view.findViewById(R.id.advertiseInformation)
        val advertiseDateTime: TextView = view.findViewById(R.id.advertiseDateTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_advertise, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var advertiseItem: Advertise = dataSet[position]
        holder.itemView.apply {
            holder.apply {
                // set src advertise image here
                advertiseDateTime.text = advertiseItem.dateTime
                advertiseInformation.text= advertiseItem.information
            }
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }
}