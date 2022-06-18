package com.example.svbookmarket.activities.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.svbookmarket.R
import com.example.svbookmarket.activities.common.Constants
import com.example.svbookmarket.activities.model.Book
import com.google.android.material.card.MaterialCardView
import java.text.DecimalFormat

class CartItemAdapter(val listener: OnButtonClickListener, private var cartList:MutableList<Book>):RecyclerView.Adapter<CartItemAdapter.VH>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val adapterLayout =
            LayoutInflater.from(parent.context).inflate(R.layout.card_checkout, parent, false)
        return VH(adapterLayout)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.title.text = cartList[position].Name
        holder.author.text = cartList[position].SalerName

        Glide
            .with(holder.itemView)
            .load(cartList[position].Image)
            .centerCrop()
            .placeholder(Constants.DEFAULT_IMG_PLACEHOLDER)
            .into(holder.thumbnail);
        holder.des.text = cartList[position].Description
        holder.itemView.setOnClickListener {
            listener.onItemClick(cartList[position])
        }
    }

    override fun getItemCount(): Int {
        return cartList.size
    }

    fun onChange(newList: MutableList<Book>)
    {
        cartList = newList
        notifyDataSetChanged()
    }

    class VH(ViewHolder:View):RecyclerView.ViewHolder(ViewHolder){
        val thumbnail: ImageView = ViewHolder.findViewById(R.id.imgSuggest)
        val title: TextView = ViewHolder.findViewById(R.id.tvBookSuggest)
        val author: TextView = ViewHolder.findViewById(R.id.tvAuthorSuggest)
        val des: TextView = ViewHolder.findViewById(R.id.sg_description)
    }

    interface OnButtonClickListener
    {
        fun onItemClick(item: Book)
    }

}