package com.example.svbookmarket.activities.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.svbookmarket.R
import com.example.svbookmarket.activities.common.Constants.DEFAULT_IMG_PLACEHOLDER
import com.example.svbookmarket.activities.model.Book
import com.example.svbookmarket.databinding.CardBookBinding
import com.makeramen.roundedimageview.RoundedImageView

/**
 * for gridview
 */
class FeaturedAdapter(
    private val dataSet: MutableList<Book>,
    private val listener: OnBookClickLitener
) :
    RecyclerView.Adapter<FeaturedAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CardBookBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .placeholder(DEFAULT_IMG_PLACEHOLDER)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(it.bookImage)

                it.itemView.setOnClickListener { listener.onBookClick(this) }
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


    class ViewHolder(binding: CardBookBinding) : RecyclerView.ViewHolder(binding.root) {
        val bookImage: RoundedImageView = binding.BookImage
        val bookTitle: TextView = binding.bookTitle
        val bookAuthor: TextView = binding.bookAuthor
        val bookPrice: TextView = binding.bookPrice
        val bookRate: TextView = binding.bookRate
        var dominanceColor =  Color.WHITE
    }

    interface OnBookClickLitener {
        fun onBookClick(book: Book)
    }
}