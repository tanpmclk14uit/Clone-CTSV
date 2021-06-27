package com.example.svbookmarket.activities.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.svbookmarket.R
import com.example.svbookmarket.activities.common.Constants
import com.example.svbookmarket.activities.model.Cart
import com.google.android.material.card.MaterialCardView

class CheckoutAdapter(private val context: Context, private var items: MutableList<Cart>) :
    RecyclerView.Adapter<CheckoutAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout =
            LayoutInflater.from(parent.context).inflate(R.layout.card_checkout2, parent, false)
        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
//        holder.author.text = context.resources.getString(item.author)
////        holder.cover.text = context.resources.getString(item.cover)
//        holder.bookname.text = context.resources.getString(item.name)
//        holder.price.text = context.resources.getString(item.price)
//        holder.cartNumber.text = context.resources.getString(item.number)
        holder.author.text = items[position].author
//        holder.cover.text = context.resources.getString(item.cover)
        holder.bookname.text = items[position].name
        holder.price.text = items[position].price.toString() + " đ"
        holder.cartNumber.text = items[position].numbers.toString()
        Glide
            .with(holder.itemView)
            .load(items[position].imgUrl)
            .centerCrop()
            .placeholder(Constants.DEFAULT_IMG_PLACEHOLDER)
            .into(holder.imgCover);
        //check option
        holder.itemView.setOnLongClickListener() {
                (it as MaterialCardView).setChecked(!it.isChecked)
            true
            }
        }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val author: TextView = view.findViewById(R.id.tv_author)
        val price: TextView = view.findViewById(R.id.tv_price)

        //    val cover:TextView = view.findViewById(R.id.img_cover)
        val bookname: TextView = view.findViewById(R.id.tv_bookname)
        val cartNumber: TextView = view.findViewById(R.id.tv_numbers)
        val imgCover: ImageView = view.findViewById(R.id.img_cover)
    }

    fun onChange(newData: MutableList<Cart>)
    {
        items = newData
    }

}