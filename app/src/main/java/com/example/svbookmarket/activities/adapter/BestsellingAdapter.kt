package com.example.svbookmarket.activities.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.svbookmarket.R
import com.example.svbookmarket.activities.model.Book
import com.example.svbookmarket.activities.model.Feature_Item
import com.makeramen.roundedimageview.RoundedImageView

class BestsellingAdapter(
    private val lstBookCard: MutableList<Feature_Item>,
) :
    RecyclerView.Adapter<BestsellingAdapter.BestsellingViewHolder>() {
    class BestsellingViewHolder(BestsellingCard: View) : RecyclerView.ViewHolder(BestsellingCard) {
        val imgBookCover: RoundedImageView = BestsellingCard.findViewById(R.id.BookImage)
        val tvBookAuthor: TextView = BestsellingCard.findViewById(R.id.bookAuthor)
        val tvBookTitle: TextView = BestsellingCard.findViewById(R.id.bookTitle)
        val tvPrice: TextView = BestsellingCard.findViewById(R.id.bookPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BestsellingViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.card_book, parent, false)
        return BestsellingViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: BestsellingViewHolder, position: Int) {
        holder.imgBookCover.setImageResource(lstBookCard[position].imgBookCover)
        holder.tvBookAuthor.text = lstBookCard[position].textAuthor
        holder.tvPrice.text = lstBookCard[position].textPrice
        holder.tvBookTitle.text = lstBookCard[position].textBookName
    }

    override fun getItemCount() = lstBookCard.size

    interface OnBestSellingItemClickListener {
        fun onBestSellingItemClick(item: Book)
    }
}