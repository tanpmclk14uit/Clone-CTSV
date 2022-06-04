package com.example.svbookmarket.activities.adapter


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.svbookmarket.activities.common.Constants.DEFAULT_IMG_PLACEHOLDER
import com.example.svbookmarket.activities.model.Book
import com.example.svbookmarket.databinding.CardBookBinding


class CategoryDetailAdapter(
    private var items: MutableList<Book>,
    private val listener: OnCategoryClickListener
) : RecyclerView.Adapter<CategoryDetailAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            CardBookBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(items[position]) {
            holder.let { it ->
                it.title.text = Name
                it.author.text = Author
                it.price.text = Price.toString()
                it.rate.text = rate.toString()

                //load image from uri
                Glide
                    .with(holder.itemView)
                    .load(Image)
                    .centerCrop()
                    .placeholder(DEFAULT_IMG_PLACEHOLDER)
                    .into(it.img);

                //set up position touch listener
                it.itemView.setOnClickListener { itemView ->
                    listener.onCategoryItemClick(this)
                }
            }
        }
    }

    override fun getItemCount(): Int = items.size


    fun addBooks(book: List<Book>) {
        if (this.items.isNotEmpty()) {
            this.items.clear()
        }
        this.items.addAll(book)
        notifyDataSetChanged()
    }


    inner class ViewHolder(binding: CardBookBinding) : RecyclerView.ViewHolder(binding.root) {
        var title = binding.bookTitle
        var author = binding.bookAuthor
        var price = binding.bookPrice
        var rate = binding.bookRate
        var img = binding.BookImage
    }

    interface OnCategoryClickListener{
        fun onCategoryItemClick(item:Book)
    }
}