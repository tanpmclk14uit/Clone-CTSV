
package com.example.svbookmarket.activities.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.svbookmarket.R
import com.example.svbookmarket.activities.common.Constants
import com.example.svbookmarket.activities.model.Book
import com.example.svbookmarket.databinding.CardBook2Binding
import com.makeramen.roundedimageview.RoundedImageView

/**
 * for linear layout view
 */

class AnotherFeaturedAdapter(
    private val dataSet: MutableList<Book>,
    private val listener: OnAnotherBookClickLitener
) :
    RecyclerView.Adapter<AnotherFeaturedAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CardBook2Binding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(dataSet[holder.adapterPosition]) {
            holder.let {
                it.bookTitle.text = Name
                it.bookAuthor.text = Author
                it.bookPrice.text =
                    holder.itemView.context.getString(R.string.price_format_vn, Price.toString())
                it.bookRate.text = rate.toString()

                Glide
                    .with(holder.itemView)
                    .load(Image)
                    .centerCrop()
                    .placeholder(Constants.DEFAULT_IMG_PLACEHOLDER)
                    .into(it.bookImage);

                it.itemView.setOnClickListener{listener.onAnotherBookClick(this)}
            }
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    fun addBooks(book: List<Book>) {
        if (this.dataSet.isNotEmpty()) {
            this.dataSet.clear()
        }
        this.dataSet.addAll(book)
        notifyDataSetChanged()
    }


    class ViewHolder(binding: CardBook2Binding) : RecyclerView.ViewHolder(binding.root) {
        val bookImage: RoundedImageView = binding.BookImage
        val bookTitle: TextView =binding.bookTitle
        val bookAuthor: TextView = binding.bookAuthor
        val bookPrice: TextView = binding.bookPrice
        val bookRate: TextView = binding.bookRate
    }

    interface OnAnotherBookClickLitener {
        fun onAnotherBookClick(book: Book)
    }
}